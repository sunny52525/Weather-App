package com.shaun.weatherapp

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.util.*

private const val TAG = "Parse Json Data"

class parseWeatherData(private val listener: onDataParsed) {
    interface onDataParsed {
        fun OnDataParsed(data: WeatherData)
        fun onError(exception: Exception)
    }

    private fun parseInBackground(rawData: String): WeatherData? {
        Log.d(TAG, "parseInBackground: Starts")

        try {
            val jsonWeather = JSONObject(rawData)

            val weather = jsonWeather.getJSONArray("weather").getJSONObject(0)

            val description = weather.getString("description")
            val icon = "https://openweathermap.org/img/wn/" + weather.getString("icon") + "@2x.png"

            val main = jsonWeather.getJSONObject("main")
            var temp = main.getString("temp") + " °C"
            var feelsLike = main.getString("feels_like") + " °C"
            val tempMax = main.getString("temp_max") + " °C"
            val tempMin = main.getString("temp_min") + " °C"

            val pressure = main.getString("pressure") + " hPa"
            val humidity = main.getString("humidity") + " %"
            val countryCode = jsonWeather.getJSONObject("sys").getString("country")
            val cityName = jsonWeather.getString("name")
            val wind = jsonWeather.getJSONObject("wind")
            val windSpeed = wind.getString("speed") + " m/s"
            val windDegree = wind.getString("deg") + " degrees"

            val suns = jsonWeather.getJSONObject("sys").getLong("sunset")
            val sdf = java.text.SimpleDateFormat("HH:mm")
            var date = Date(suns * 1000L)
            sdf.format(date)
            val sunsetTime = date.toString()
            val sunr = jsonWeather.getJSONObject("sys").getLong("sunrise")
            date = java.util.Date(sunr * 1000)
            sdf.format(date)
            val sunriseTime = date.toString()
            val weatherData: WeatherData = WeatherData(
                description,
                icon,
                temp,
                feelsLike,
                pressure,
                humidity,
                tempMax,
                tempMin,
                countryCode,
                cityName,
                windSpeed,
                windDegree,
                sunriseTime,
                sunsetTime
            )
            Log.d(TAG, "parseInBackground: $weatherData")
            return weatherData
        } catch (e: Exception) {

            listener.onError(e)
            Log.d(TAG, "parseInBackground: Error ${e}")
            return null
        }

    }

    fun parseData(rawData: String) {
        Log.d(TAG, "parseData: $rawData")
        GlobalScope.launch {
            val result = parseInBackground(rawData)
            withContext(Dispatchers.Main) {
                result?.let { listener.OnDataParsed(it) }
            }
        }
    }
}