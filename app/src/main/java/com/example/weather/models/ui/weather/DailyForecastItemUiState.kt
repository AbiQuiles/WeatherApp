package com.example.weather.models.ui.weather

data class DailyForecastItemUiState(
    val day: String = "Nov 9",
    val forecastUrlImage: String = "",
    val highAndLowTemp: Pair<String, String> = Pair("200°", "-200°")
)