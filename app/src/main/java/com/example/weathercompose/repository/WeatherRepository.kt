package com.example.weathercompose.repository

import com.example.weathercompose.data.WeatherInfo

interface WeatherRepository {
    suspend fun getWeather(city: String): WeatherInfo
}