package com.example.weather.data

class DataOrException<D, Boolean, E: Exception>(
    var data: D? = null,
    var loading: Boolean? = null,
    var e: E? = null
)