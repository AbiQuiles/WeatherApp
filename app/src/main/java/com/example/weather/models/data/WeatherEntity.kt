package com.example.weather.models.data

data class WeatherEntity(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherLarge>,
    val message: Double
)