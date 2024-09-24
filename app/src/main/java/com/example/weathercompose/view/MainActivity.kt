package com.example.weathercompose.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.weathercompose.data.WeatherDetails
import com.example.weathercompose.data.WeatherMainCard
import com.example.weathercompose.data.Forecast
import com.example.weathercompose.ui.theme.WeatherComposeTheme
import com.example.weathercompose.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()

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
                    mutableStateOf(
                        WeatherMainCard(
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                        )
                    )
                }
                val weatherDetails = remember {
                    mutableStateOf(
                        WeatherDetails(
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                        )
                    )
                }

                Background()

                Column {
                    MainCard(weatherMainCard)
                    TabLayout(forecast, weatherDetails)
                }

                LaunchedEffect(city.value) {
                    weatherViewModel.fetchWeatherData(city.value)
                }

                weatherViewModel.weatherInfoResult.observe(this) { weatherInfo ->
                    forecast.value = weatherInfo.forecast
                    weatherMainCard.value = weatherInfo.weatherMainCard
                    weatherDetails.value = weatherInfo.weatherDetails
                }
            }
        }
    }
}

