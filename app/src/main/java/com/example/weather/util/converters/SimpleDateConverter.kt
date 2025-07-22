package com.example.weather.util.converters

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

class SimpleDateConverter {

    @SuppressLint("SimpleDateFormat")
    fun formatDate(timestamp: Int): String {
        val converter = SimpleDateFormat("EEE, MMM d")
        val date = Date(timestamp.toLong() * 1000)

        return converter.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDateTime(timestamp: Int): String {
        val converter = SimpleDateFormat("hh:mm:aa")
        val date = Date(timestamp.toLong() * 1000)

        return converter.format(date)
    }

    fun formatDecimals(item: Double): String {
        return " %.0f".format(item)
    }
}