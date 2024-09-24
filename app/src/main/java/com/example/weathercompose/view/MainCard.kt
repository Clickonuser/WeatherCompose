package com.example.weathercompose.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weathercompose.R
import com.example.weathercompose.data.WeatherMainCard
import com.example.weathercompose.ui.theme.ThemeWeather
import kotlin.math.roundToInt

@Composable
fun MainCard(weatherMainCard: MutableState<WeatherMainCard>, onClickSync: () -> Unit, onClickSearch: () -> Unit) {

    val city = weatherMainCard.value.city.ifEmpty { "-" }
    val date = weatherMainCard.value.date.ifEmpty { "-" }
    val icon = "https:" + weatherMainCard.value.icon
    val currentTemp = "${weatherMainCard.value.currentTemp.toFloatOrNull()?.roundToInt() ?: 0}°C"
    val condition = weatherMainCard.value.condition.ifEmpty { "-" }
    val maxMin = "${weatherMainCard.value.maxTemp.toFloatOrNull()?.roundToInt() ?: 0}°/" +
            "${weatherMainCard.value.minTemp.toFloatOrNull()?.roundToInt() ?: 0}°"

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
                    text = city,
                    style = TextStyle(fontSize = 16.sp, color = Color.White)
                )
                Text(
                    modifier = Modifier.padding(end = 8.dp, top = 8.dp),
                    text = date,
                    style = TextStyle(fontSize = 16.sp, color = Color.White)
                )
            }
            AsyncImage(
                modifier = Modifier
                    .size(64.dp)
                    .padding(top = 3.dp, end = 8.dp),
                model = icon,
                contentDescription = "im condition",
            )
            Text(
                text = currentTemp,
                style = TextStyle(fontSize = 48.sp, color = Color.White)
            )
            Text(
                text = condition,
                style = TextStyle(fontSize = 16.sp, color = Color.White),
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    onClickSearch.invoke()
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
                        text = maxMin,
                        style = TextStyle(fontSize = 16.sp, color = Color.White)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_down),
                        contentDescription = "ic down"
                    )
                }
                IconButton(onClick = {
                    onClickSync.invoke()
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