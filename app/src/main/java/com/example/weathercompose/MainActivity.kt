package com.example.weathercompose

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weathercompose.data.WeatherDetails
import com.example.weathercompose.data.WeatherMainCard
import com.example.weathercompose.data.Forecast
import com.example.weathercompose.data.WeatherResponse
import com.example.weathercompose.ui.theme.ThemeWeather
import com.example.weathercompose.ui.theme.WeatherComposeTheme
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

const val API_KEY = "df1c637cf2e6440f982132240241907"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherComposeTheme {
                val city = remember {
                    mutableStateOf("Tokyo")
                }
                val forecast = remember {
                    mutableStateOf(listOf<Forecast>())
                }
                val weatherMainCard = remember {
                    mutableStateOf(WeatherMainCard(
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                    ))
                }
                val weatherDetails = remember {
                    mutableStateOf(WeatherDetails(
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                    ))
                }
                getData(city.value, this, forecast, weatherMainCard, weatherDetails)
                Image(
                    painter = painterResource(
                        id = R.drawable.back
                    ),
                    contentDescription = "im1",
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.5f),
                    contentScale = ContentScale.Crop
                )
                Column {
                    MainCard()
                    TabLayout()
                }
            }
        }
    }
}

private fun getData(
    city: String,
    context: Context,
    forecast: MutableState<List<Forecast>>,
    weatherMainCard: MutableState<WeatherMainCard>,
    weatherDetails: MutableState<WeatherDetails>
) {
    val url = "https://api.weatherapi.com/v1/forecast.json?key=$API_KEY" +
            "&q=$city" +
            "&days=" +
            "3" +
            "&aqi=no&alerts=no"
    val queue = Volley.newRequestQueue(context)
    val sRequest = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            val result = parseData(response)
            forecast.value = result.forecast
            weatherMainCard.value = result.weatherMainCard
            weatherDetails.value = result.weatherDetails
            Toast.makeText(context, response, Toast.LENGTH_SHORT).show()
        },
        { error ->

        }
    )
    queue.add(sRequest)
}

private fun parseData(response: String): WeatherResponse {
    val mainObject = JSONObject(response)
    val currentObject = mainObject.getJSONObject("current")
    val forecastArray = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
    val item = forecastArray[0] as JSONObject

    val mainCard = parseMainCard(mainObject, currentObject, item)
    val weatherDetails = parseWeatherDetails(currentObject, item)
    val forecast = parseForecast(forecastArray)

    return WeatherResponse(
        mainCard,
        forecast,
        weatherDetails
    )
}

private fun parseWeatherDetails(currentObject: JSONObject, item: JSONObject): WeatherDetails {
    return WeatherDetails(
        item.getJSONObject("astro").getString("sunrise"),
        item.getJSONObject("astro").getString("sunset"),
        currentObject.getString("wind_kph"),
        item.getJSONObject("day").getString("daily_chance_of_rain"),
        item.getJSONObject("day").getString("daily_chance_of_snow"),
        currentObject.getString("humidity"),
        currentObject.getString("cloud"),
    )
}

private fun parseMainCard(mainObject: JSONObject, currentObject: JSONObject, item: JSONObject): WeatherMainCard {
    return WeatherMainCard(
        mainObject.getJSONObject("location").getString("name"),
        item.getString("date"),
        currentObject.getString("temp_c"),
        item.getJSONObject("day").getString("maxtemp_c"),
        item.getJSONObject("day").getString("mintemp_c"),
        currentObject.getJSONObject("condition").getString("text"),
        currentObject.getJSONObject("condition").getString("icon")
    )
}

private fun parseForecast(forecastArray: JSONArray): List<Forecast> {
    val listForecast = ArrayList<Forecast>()
    for (i in 0 until forecastArray.length()) {
        val el = forecastArray[i] as JSONObject
        val forecastByHoursList = el.getJSONArray("hour")
        for (j in 0 until forecastByHoursList.length()) {
            val itemHour = forecastByHoursList[j] as JSONObject
            listForecast.add(
                Forecast(
                    itemHour.getString("time"),
                    itemHour.getString("temp_c"),
                    itemHour.getJSONObject("condition").getString("text"),
                    itemHour.getJSONObject("condition").getString("icon")
                )
            )
        }
    }
    return listForecast
}

@Composable
private fun MainCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = ThemeWeather
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                    text = "city",
                    style = TextStyle(fontSize = 16.sp, color = Color.White)
                )
                Text(
                    modifier = Modifier.padding(end = 8.dp, top = 8.dp),
                    text = "date",
                    style = TextStyle(fontSize = 16.sp, color = Color.White)
                )
            }
            AsyncImage(
                modifier = Modifier
                    .size(64.dp)
                    .padding(top = 3.dp, end = 8.dp),
                model = "https://cdn.weatherapi.com/weather/64x64/day/176.png",
                contentDescription = "im condition",
            )
            Text(
                text = "0°C",
                style = TextStyle(fontSize = 57.sp, color = Color.White)
            )
            Text(
                text = "condition",
                style = TextStyle(fontSize = 16.sp, color = Color.White),
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {

                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "ic search",
                        tint = Color.White
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_up),
                        contentDescription = "ic up"
                    )
                    Text(
                        text = "0°/0°",
                        style = TextStyle(fontSize = 16.sp, color = Color.White)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_down),
                        contentDescription = "ic down"
                    )
                }
                IconButton(onClick = {

                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sync),
                        contentDescription = "ic sync",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayout() {
    val tabList = listOf("FORECAST", "DETAILS")
    val pagerState = rememberPagerState {
        tabList.size
    }
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(
                start = 8.dp,
                end = 8.dp
            )
            .clip(RoundedCornerShape(10.dp))
    ) {
        TabRow(
            containerColor = ThemeWeather,
            contentColor = Color.White,
            selectedTabIndex = tabIndex,
            indicator = { pos ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(pos[tabIndex]),
                    color = Color.White
                )
            }
        ) {
            tabList.forEachIndexed { index, text ->
                Tab(
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(text = text)
                    }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1.0f)
        ) { index ->
            when (index) {
                0 -> MainList()
                1 -> Details()
            }
        }
    }
}

@Composable
fun MainList() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(20) {
            ListItem()
        }
    }
}

@Composable
fun ListItem() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = ThemeWeather
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(start = 8.dp, top = 5.dp, bottom = 5.dp)
            ) {
                Text(
                    text = "date and time",
                    style = TextStyle(fontSize = 16.sp)
                )
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = "condition",
                    color = Color.White, style = TextStyle(fontSize = 16.sp)
                )
            }
            Text(
                text = "0°",
                color = Color.White,
                style = TextStyle(fontSize = 20.sp)
            )
            AsyncImage(
                modifier = Modifier
                    .size(38.dp)
                    .padding(end = 8.dp),
                model = "https://cdn.weatherapi.com/weather/64x64/day/176.png",
                contentDescription = "im condition",
            )

        }

    }
}

@Composable
fun Details() {
    ConstraintLayout(
        modifier = Modifier
            .padding(top = 4.dp)
            .fillMaxSize()
            .background(ThemeWeather)
    ) {
        val (iconSunrise, textSunrise, iconSunset, textSunset, line, textLastUpdated, dataColumn) = createRefs()
        val centerVerticalGuideLine = createGuidelineFromStart(0.5f)

        Icon(
            modifier = Modifier.constrainAs(iconSunrise) {
                top.linkTo(parent.top, 24.dp)
                start.linkTo(parent.start, 8.dp)
                end.linkTo(centerVerticalGuideLine)
            },
            painter = painterResource(id = R.drawable.ic_sunrise),
            contentDescription = "ic sunrise",
            tint = Color.White
        )
        Icon(
            modifier = Modifier.constrainAs(line) {
                top.linkTo(iconSunrise.top)
                bottom.linkTo(textSunrise.bottom)
                start.linkTo(centerVerticalGuideLine)
                end.linkTo(centerVerticalGuideLine)
            },
            painter = painterResource(id = R.drawable.ic_line),
            contentDescription = "ic line",
            tint = Color.White
        )
        Icon(
            modifier = Modifier.constrainAs(iconSunset) {
                top.linkTo(iconSunrise.top)
                bottom.linkTo(iconSunrise.bottom)
                start.linkTo(centerVerticalGuideLine)
                end.linkTo(parent.end, 8.dp)
            },
            painter = painterResource(id = R.drawable.ic_sunset),
            contentDescription = "ic sunset",
            tint = Color.White
        )
        Text(
            modifier = Modifier.constrainAs(textSunrise) {
                top.linkTo(iconSunrise.bottom, 4.dp)
                start.linkTo(iconSunrise.start)
                end.linkTo(iconSunrise.end)
            },
            text = buildAnnotatedString {
                append("Sunrise:\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("0:00 AM")
                }
            },
            color = Color.White,
            style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center)
        )
        Text(
            modifier = Modifier.constrainAs(textSunset) {
                top.linkTo(iconSunset.bottom, 4.dp)
                start.linkTo(iconSunset.start)
                end.linkTo(iconSunset.end)
            },
            text = buildAnnotatedString {
                append("Sunset:\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("0:00 PM")
                }
            },
            color = Color.White,
            style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center)
        )

        Column(
            modifier = Modifier.constrainAs(dataColumn) {
                top.linkTo(textSunrise.bottom, 32.dp)
                start.linkTo(centerVerticalGuideLine)
                end.linkTo(centerVerticalGuideLine)
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = buildAnnotatedString {
                    append("Wind speed: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("0 km/h")
                    }
                },
                color = Color.White,
                style = TextStyle(fontSize = 16.sp)
            )
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = buildAnnotatedString {
                    append("Chance of rain: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("0 %")
                    }
                },
                color = Color.White,
                style = TextStyle(fontSize = 16.sp)
            )
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = buildAnnotatedString {
                    append("Chance of snow: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("0 %")
                    }
                },
                color = Color.White,
                style = TextStyle(fontSize = 16.sp)
            )
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = buildAnnotatedString {
                    append("Humidity: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("0 %")
                    }
                },
                color = Color.White,
                style = TextStyle(fontSize = 16.sp)
            )
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = buildAnnotatedString {
                    append("cloud: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("0 %")
                    }
                },
                color = Color.White,
                style = TextStyle(fontSize = 16.sp)
            )
        }
        Text(
            modifier = Modifier.constrainAs(textLastUpdated) {
                top.linkTo(dataColumn.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(centerVerticalGuideLine)
                end.linkTo(centerVerticalGuideLine)
            },
            text = "last updated 0 minutes ago",
            color = Color.White.copy(0.64f),
            style = TextStyle(fontSize = 12.sp)
        )
    }
}