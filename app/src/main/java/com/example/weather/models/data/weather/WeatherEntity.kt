package com.example.weather.models.data.weather

data class WeatherEntity(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherLarge>,
    val message: Double
)