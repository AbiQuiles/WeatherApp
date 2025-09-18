package com.example.weather.util.converters

import kotlin.io.encoding.Base64

fun decode(key: String): String {
    val decodedBytes = Base64.decode(key)
    val encryptedString = String(decodedBytes, Charsets.UTF_8)

    return encryptedString
}