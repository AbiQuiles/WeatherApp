package com.example.weather.repository

import android.util.Log
import com.example.weather.models.data.weather.WeatherDto
import com.example.weather.models.mappers.toUiModel
import com.example.weather.models.ui.weather.CurrentWeatherUiState
import com.example.weather.models.ui.weather.DailyForecastItemUiState
import com.example.weather.models.ui.weather.WeatherScreenUiState
import com.example.weather.network.WeatherApi
import com.example.weather.network.remote.config.RemoteConfigManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val remoteConfigManager: RemoteConfigManager,
    private val api: WeatherApi
) {

    fun getWeather(locationQuery: String): Flow<WeatherScreenUiState> = flow {
        emit(WeatherScreenUiState(isLoading = true))

        try {
            println("Rez WeathRepo $${remoteConfigManager.weatherApiKey}")
            val weatherDto: WeatherDto = api.getWeather(
                query = locationQuery,
                appid = remoteConfigManager.weatherApiKey
            )
            val currentWeatherUiState: CurrentWeatherUiState = weatherDto.toUiModel()
            val dailyForecastItemUiStates: List<DailyForecastItemUiState> = weatherDto.list
                .slice(1..6).map { weatherLargeDto ->
                    weatherLargeDto.toUiModel()
                }

            Log.d("WeatherRepository", "Fetch & Map Success for $locationQuery")

            emit(WeatherScreenUiState(
                isLoading = false,
                currentWeather = currentWeatherUiState,
                weekWeekForecast = dailyForecastItemUiStates
            ))

        } catch (e: Exception) {
            Log.e("WeatherRepository", "API Error: ${e.message}", e)

            emit(WeatherScreenUiState(
                isLoading = false,
                isError = true
            ))
        }
    }
}