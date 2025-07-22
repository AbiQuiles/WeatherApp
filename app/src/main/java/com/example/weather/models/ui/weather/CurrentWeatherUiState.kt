package com.example.weather.models.ui.weather

data class CurrentWeatherUiState(
    val city: String = "Orlando",
    val currentTemp: String = "83",
    val tempDescription: String = "Sunny",
    val feelsLike: String = "86",
    val highAndLowTemp: Pair<String, String> = Pair("200°", "-200°")
)