package com.example.weathercompose.data

data class WeatherResponse(
    val weatherMainCard: WeatherMainCard,
    val forecast: List<Forecast>,
    val weatherDetails: WeatherDetails
)
