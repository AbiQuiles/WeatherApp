package com.example.weather.network

import com.example.weather.util.converters.decode

object NetworkConstants {
    const val BASE_URL = "https://api.openweathermap.org/"
    val API_KEY: String by lazy { decode("ZWQ2MGZjZmJkMTEwZWU2NWM3MTUwNjA1ZWE4YWNlZWE=") }
}