package com.example.weathercompose

import android.os.Bundle
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
import com.example.weathercompose.ui.theme.IndicatorWeather
import com.example.weathercompose.ui.theme.ThemeWeather
import com.example.weathercompose.ui.theme.WeatherComposeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherComposeTheme {
                Column {
                    MainCard()
                    TabLayout()
                }
            }
        }
    }
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
                    color = IndicatorWeather
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