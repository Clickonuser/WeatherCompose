package com.example.weathercompose.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathercompose.data.WeatherInfo
import com.example.weathercompose.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weatherInfo = MutableLiveData<WeatherInfo>()
    val weatherInfoResult: LiveData<WeatherInfo> = _weatherInfo

    fun fetchWeatherData(city: String) {
        viewModelScope.launch {
            try {
                val weatherInfo: WeatherInfo = repository.getWeather(city)
                _weatherInfo.postValue(weatherInfo)
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }
}