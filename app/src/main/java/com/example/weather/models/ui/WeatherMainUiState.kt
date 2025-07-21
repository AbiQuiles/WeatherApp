package com.example.weather.models.ui

data class WeatherMainUiState(
    val city: String = "Orlando",
    val currentTemp: String = "83",
    val tempDescription: String = "Sunny",
    val feelsLike: String = "86",
    val highAndLowTemp: Pair<String, String> = Pair("89°", "75°")
)

data class WeatherDailyForecastItemUiState(
    val day: String,
    val foreCaseImage: String,
    val dayMaxTemp: String,
    val dayMinTamp: String
)
