package com.example.weather.network

import com.example.weather.models.data.weather.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface WeatherApi {

    @GET(value = "data/2.5/forecast/daily")
    suspend fun getWeather(
        @Query("q") query: String,
        @Query("units") units: String = "imperial",
        @Query("appid") appid: String //= "ed60fcfbd110ee65c7150605ea8aceea"
    ): WeatherDto
}