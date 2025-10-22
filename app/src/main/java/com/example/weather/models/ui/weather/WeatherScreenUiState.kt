package com.example.weather.models.ui.weather

import com.example.weather.widgets.BasicUiStates

data class WeatherScreenUiState(
    override val isLoading: Boolean = false,
    override val isOffline: Boolean = false,
    override val isError: Boolean = false,
    val currentWeather: CurrentWeatherUiState? = null,
    val weekWeekForecast: List<DailyForecastItemUiState> = emptyList(),
): BasicUiStates
