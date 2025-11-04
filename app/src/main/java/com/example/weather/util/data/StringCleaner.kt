package com.example.weather.util.data

fun cleanString(rawString: String): String {
    val unwantedSymbolsRegex = Regex("[\\s\\-().°]+")
    return rawString
        .trim()
        .lowercase()
        .replace(unwantedSymbolsRegex, "")
}