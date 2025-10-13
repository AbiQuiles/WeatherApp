package com.example.weather.util.data

fun cleanString(dirtyName: String): String {
    val unwantedSymbolsRegex = Regex("[\\s\\-()\\.]+")
    return dirtyName
        .trim()
        .lowercase()
        .replace(unwantedSymbolsRegex, "")
}