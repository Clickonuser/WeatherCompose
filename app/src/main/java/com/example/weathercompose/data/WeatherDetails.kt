package com.example.weathercompose.data

data class WeatherDetails(
    val sunrise: String,
    val sunset: String,
    val windSpeed: String,
    var chanceOfRain: String,
    val chanceOfSnow: String,
    val humidity: String,
    val cloud: String,
)
