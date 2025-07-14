package com.example.weather.repository

import android.util.Log
import com.example.weather.data.DataOrException
import com.example.weather.models.Weather
import com.example.weather.network.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherApi) {

    suspend fun getWeather(cityQuery: String): DataOrException<
            Weather,
            Boolean,
            Exception
    > {
        val response = try {

            api.getWeather(query = cityQuery)

        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error: $e")
            return DataOrException(e = e)
        }

        return DataOrException(data = response)
    }
}