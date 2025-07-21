package com.example.weather.models

import com.example.weather.models.data.WeatherEntity
import com.example.weather.models.ui.WeatherMainUiState
import kotlin.math.roundToInt

class ModelConverter {

    fun weatherEntityToMainUiState(weatherEntity: WeatherEntity): WeatherMainUiState {
        val weatherLarge = weatherEntity.list.first()
        val weatherSmall = weatherLarge.weather.first()

        val city = weatherEntity.city.name
        val currentTemp =  convertTempToUiModel(weatherLarge.temp.day)
        val tempDescription = capFirstCharInWords(weatherSmall.description)
        val feelsLike = convertTempToUiModel(weatherLarge.feels_like.day)
        val dayMaxTemp = convertTempToUiModel(weatherLarge.temp.max)
        val dayMinTemp = convertTempToUiModel(weatherLarge.temp.min)
        val highAndLowTemp = Pair(dayMaxTemp, dayMinTemp)

        return WeatherMainUiState(
            city = city,
            currentTemp = currentTemp,
            tempDescription = tempDescription,
            feelsLike = feelsLike,
            highAndLowTemp = highAndLowTemp,
        )
    }
}

private fun convertTempToUiModel(temp: Double) =
    temp.roundToInt().toString()

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