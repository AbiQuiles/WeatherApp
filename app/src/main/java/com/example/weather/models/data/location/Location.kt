package com.example.weather.models.data.location

data class Location(
    val coord: Coord,
    val country: String,
    val geoname: Geoname,
    val id: Double,
    val langs: List<Lang>,
    val name: String,
    val stat: Stat,
    val stations: List<Station>,
    val zoom: Double
)