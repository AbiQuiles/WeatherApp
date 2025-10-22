package com.example.weather.models.mappers

import android.annotation.SuppressLint
import com.example.weather.models.data.weather.WeatherDto
import com.example.weather.models.data.weather.WeatherLargeDto
import com.example.weather.models.ui.weather.CurrentWeatherUiState
import com.example.weather.models.ui.weather.DailyForecastItemUiState
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt

fun WeatherDto.toUiModel(): CurrentWeatherUiState {
        val weatherLarge = this.list.first()
        val weatherSmall = weatherLarge.weather.first()

        val city = this.city.name
        val currentTemp = convertTempToUiModel(weatherLarge.temp.day)
        val tempDescription = capFirstCharInWords(weatherSmall.description)
        val feelsLike = convertTempToUiModel(weatherLarge.feels_like.day)
        val dayMaxTemp = convertTempToUiModel(weatherLarge.temp.max)
        val dayMinTemp = convertTempToUiModel(weatherLarge.temp.min)
        val highAndLowTemp = Pair(dayMaxTemp, dayMinTemp)

        return CurrentWeatherUiState(
            city = city,
            currentTemp = currentTemp,
            tempDescription = tempDescription,
            feelsLike = feelsLike,
            highAndLowTemp = highAndLowTemp,
        )
}

fun WeatherLargeDto.toUiModel(): DailyForecastItemUiState {
    val weatherSmall = this.weather.first()
    val imageUrlKey = weatherSmall.icon

    val day = formatDate(this.dt)
    val forecastUrlImage = "https://openweathermap.org/img/wn/${imageUrlKey}.png"
    val dayMaxTemp = convertTempToUiModel(this.temp.max)
    val dayMinTemp = convertTempToUiModel(this.temp.min)
    val highAndLowTemp = Pair(dayMaxTemp, dayMinTemp)

    return DailyForecastItemUiState(
        day = day,
        forecastUrlImage = forecastUrlImage,
        highAndLowTemp = highAndLowTemp
    )
}

/** Helper Functions*/

@SuppressLint("SimpleDateFormat")
private fun formatDate(timestamp: Int): String {
    val converter = SimpleDateFormat("EEE, MMM d")
    val date = Date(timestamp.toLong() * 1000)

    return converter.format(date)
}

private fun convertTempToUiModel(temp: Double) =
    temp.roundToInt().toString().plus("°")

private fun capFirstCharInWords(string: String): String {
    if (string.isBlank()) {
        return string
    }
    return string.split(' ')
        .joinToString(" ") { word ->
            word.replaceFirstChar {
                if (it.isLowerCase())
                    it.titlecase()
                else
                    it.toString()
            }
        }
}