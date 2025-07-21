package com.example.weather.models.data

data class WeatherSmall(
    val description: String, // Weather State but more descriptive (light rain
    val icon: String,
    val id: Int,
    val main: String // Weather State (Rain, Snow, Cloud)
)