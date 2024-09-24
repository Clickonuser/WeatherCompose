package com.example.weathercompose.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.weathercompose.R
import com.example.weathercompose.data.Forecast
import com.example.weathercompose.data.WeatherDetails
import com.example.weathercompose.ui.theme.ThemeWeather
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabLayout(
    listForecast: MutableState<List<Forecast>>,
    weatherDetails: MutableState<WeatherDetails>
) {
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
                0 -> ForecastList(listForecast = listForecast.value)
                1 -> Details(weatherDetails = weatherDetails)
            }
        }
    }
}

@Composable
private fun ForecastList(listForecast: List<Forecast>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        itemsIndexed(listForecast) { index, _ ->
            ListItem(item = listForecast[index])
        }
    }
}

@Composable
private fun ListItem(item: Forecast) {

    val date = item.date.ifEmpty { "-" }
    val condition = item.condition.ifEmpty { "-" }
    val temp = "${item.temp.toFloatOrNull()?.roundToInt() ?: 0}°C"
    val icon = "https:" + item.icon

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
                    text = date,
                    style = TextStyle(fontSize = 16.sp),
                )
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = condition,
                    color = Color.White, style = TextStyle(fontSize = 16.sp)
                )
            }
            Text(
                text = temp,
                color = Color.White,
                style = TextStyle(fontSize = 20.sp)
            )
            AsyncImage(
                modifier = Modifier
                    .size(38.dp)
                    .padding(end = 8.dp),
                model = icon,
                contentDescription = "im condition",
            )

        }

    }
}

@Composable
private fun Details(weatherDetails: MutableState<WeatherDetails>) {
    val sunriseTime = weatherDetails.value.sunrise.ifEmpty { "-" }
    val sunsetTime = weatherDetails.value.sunset.ifEmpty { "-" }
    val windSpeed = "${weatherDetails.value.windSpeed.toFloatOrNull()?.roundToInt() ?: 0}"
    val chanceOfRain = weatherDetails.value.chanceOfRain.ifEmpty { "-" }
    val chanceOfSnow = weatherDetails.value.chanceOfSnow.ifEmpty { "-" }
    val humidity = weatherDetails.value.humidity.ifEmpty { "-" }
    val cloud = weatherDetails.value.cloud.ifEmpty { "-" }

    ConstraintLayout(
        modifier = Modifier
            .padding(top = 4.dp)
            .fillMaxSize()
            .background(ThemeWeather)
    ) {
        val (iconSunrise, textSunrise, iconSunset, textSunset, line, dataColumn) = createRefs()
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
                    append(sunriseTime)
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
                    append(sunsetTime)
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
                modifier = Modifier.padding(bottom = 6.dp),
                text = buildAnnotatedString {
                    append("Wind speed: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("$windSpeed km/h")
                    }
                },
                color = Color.White,
                style = TextStyle(fontSize = 16.sp)
            )
            Text(
                modifier = Modifier.padding(bottom = 6.dp),
                text = buildAnnotatedString {
                    append("Chance of rain: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("$chanceOfRain %")
                    }
                },
                color = Color.White,
                style = TextStyle(fontSize = 16.sp)
            )
            Text(
                modifier = Modifier.padding(bottom = 6.dp),
                text = buildAnnotatedString {
                    append("Chance of snow: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("$chanceOfSnow %")
                    }
                },
                color = Color.White,
                style = TextStyle(fontSize = 16.sp)
            )
            Text(
                modifier = Modifier.padding(bottom = 6.dp),
                text = buildAnnotatedString {
                    append("Humidity: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("$humidity %")
                    }
                },
                color = Color.White,
                style = TextStyle(fontSize = 16.sp)
            )
            Text(
                modifier = Modifier.padding(bottom = 6.dp),
                text = buildAnnotatedString {
                    append("cloud: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("$cloud %")
                    }
                },
                color = Color.White,
                style = TextStyle(fontSize = 16.sp)
            )
        }
    }
}