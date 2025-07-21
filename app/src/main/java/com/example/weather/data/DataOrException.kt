package com.example.weather.data

class DataOrException<D, E: Exception>(
    var data: D? = null,
    var e: E? = null
)