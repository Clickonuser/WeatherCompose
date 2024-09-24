package com.example.weathercompose.repository

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weathercompose.data.Forecast
import com.example.weathercompose.data.WeatherDetails
import com.example.weathercompose.data.WeatherMainCard
import com.example.weathercompose.data.WeatherInfo
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import kotlin.coroutines.resume

class WeatherRepositoryImpl @Inject constructor(
    private val context: Context
) : WeatherRepository {

   override suspend fun getWeather(city: String): WeatherInfo {
       return suspendCancellableCoroutine { continuation ->
           val url = "https://api.weatherapi.com/v1/forecast.json" +
                   "?key=$API_KEY" +
                   "&q=$city" +
                   "&days=$LIMIT" +
                   "&aqi=no" +
                   "&alerts=no"

           val queue = Volley.newRequestQueue(context)
           val sRequest = StringRequest(
               Request.Method.GET,
               url,
               { response ->
                   val weatherInfo = parseWeatherResponse(response)
                   continuation.resume(weatherInfo)
               },
               { _ ->
                   Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
               }
           )
           queue.add(sRequest)
       }
   }

    private fun parseWeatherResponse(response: String): WeatherInfo {
        val mainObject = JSONObject(response)
        val currentObject = mainObject.getJSONObject("current")
        val forecastArray = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
        val item = forecastArray[0] as JSONObject

        val mainCard = parseMainCard(mainObject, currentObject, item)
        val weatherDetails = parseWeatherDetails(currentObject, item)
        val forecast = parseForecast(forecastArray)

        return WeatherInfo(
            mainCard,
            forecast,
            weatherDetails
        )
    }

    private fun parseMainCard(
        mainObject: JSONObject,
        currentObject: JSONObject,
        item: JSONObject
    ): WeatherMainCard {
        return WeatherMainCard(
            mainObject.getJSONObject("location").getString("name"),
            item.getString("date"),
            currentObject.getJSONObject("condition").getString("icon"),
            currentObject.getString("temp_c"),
            currentObject.getJSONObject("condition").getString("text"),
            item.getJSONObject("day").getString("maxtemp_c"),
            item.getJSONObject("day").getString("mintemp_c"),
        )
    }

    private fun parseWeatherDetails(currentObject: JSONObject, item: JSONObject): WeatherDetails {
        return WeatherDetails(
            item.getJSONObject("astro").getString("sunrise"),
            item.getJSONObject("astro").getString("sunset"),
            currentObject.getString("wind_kph"),
            item.getJSONObject("day").getString("daily_chance_of_rain"),
            item.getJSONObject("day").getString("daily_chance_of_snow"),
            currentObject.getString("humidity"),
            currentObject.getString("cloud"),
        )
    }

    private fun parseForecast(forecastArray: JSONArray): List<Forecast> {
        val listForecast = ArrayList<Forecast>()
        for (i in 0 until forecastArray.length()) {
            val el = forecastArray[i] as JSONObject
            val forecastByHoursList = el.getJSONArray("hour")
            for (j in 0 until forecastByHoursList.length()) {
                val itemHour = forecastByHoursList[j] as JSONObject
                listForecast.add(
                    Forecast(
                        itemHour.getString("time"),
                        itemHour.getJSONObject("condition").getString("text"),
                        itemHour.getString("temp_c"),
                        itemHour.getJSONObject("condition").getString("icon")
                    )
                )
            }
        }
        return listForecast
    }

    companion object {
        const val LIMIT = "3"
        const val API_KEY = "df1c637cf2e6440f982132240241907"
    }
}