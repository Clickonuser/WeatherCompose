package com.example.weathercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weathercompose.ui.theme.ThemeWeather
import com.example.weathercompose.ui.theme.WeatherComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherComposeTheme {
                MainCard()
            }
        }
    }
}

@Composable
private fun MainCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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