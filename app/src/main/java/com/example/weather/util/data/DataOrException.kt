package com.example.weather.util.data

class DataOrException<D, E: Exception>(
    var data: D? = null,
    var e: E? = null
)