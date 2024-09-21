package com.example.weathercompose.data

data class WeatherMainCard(
    val city: String,
    val date: String,
    val icon: String,
    var currentTemp: String,
    val condition: String,
    val maxTemp: String,
    val minTemp: String,
)
