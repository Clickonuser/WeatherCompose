package com.example.weathercompose.data

data class WeatherInfo(
    val weatherMainCard: WeatherMainCard,
    val forecast: List<Forecast>,
    val weatherDetails: WeatherDetails
)
