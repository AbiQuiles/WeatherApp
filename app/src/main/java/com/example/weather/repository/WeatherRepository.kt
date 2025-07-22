package com.example.weather.repository

import android.util.Log
import com.example.weather.util.data.DataOrException
import com.example.weather.models.data.WeatherEntity
import com.example.weather.network.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherApi) {

    suspend fun getWeather(cityQuery: String): DataOrException<WeatherEntity, Exception> {
        val response = try {
            api.getWeather(query = cityQuery)

        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error: $e")
            return DataOrException(e = e)
        }

        Log.d("WeatherRepository", "Fetch Success: $response")
        return DataOrException(data = response)
    }
}