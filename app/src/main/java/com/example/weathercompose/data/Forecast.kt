package com.example.weathercompose.data

data class Forecast(
    val date: String,
    val condition: String,
    var temp: String,
    val icon: String,
)
