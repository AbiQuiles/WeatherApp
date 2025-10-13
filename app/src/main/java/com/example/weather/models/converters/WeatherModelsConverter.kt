package com.example.weather.models.converters

import android.annotation.SuppressLint
import com.example.weather.models.data.weather.WeatherDto
import com.example.weather.models.data.weather.WeatherLarge
import com.example.weather.models.ui.location.SearchResultItemUiState
import com.example.weather.models.ui.weather.CurrentWeatherUiState
import com.example.weather.models.ui.weather.DailyForecastItemUiState
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt

class WeatherModelsConverter {

    fun weatherDtoToCurrentWeatherUiState(weatherDto: WeatherDto): CurrentWeatherUiState {
        val weatherLarge = weatherDto.list.first()
        val weatherSmall = weatherLarge.weather.first()

        val city = weatherDto.city.name
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

    fun weatherLargeToDailyIForecastItemUiState(weatherLarge: WeatherLarge): DailyForecastItemUiState {
        val weatherSmall = weatherLarge.weather.first()
        val imageUrlKey = weatherSmall.icon

        val day = formatDate(weatherLarge.dt)
        val forecastUrlImage = "https://openweathermap.org/img/wn/${imageUrlKey}.png"
        val dayMaxTemp = convertTempToUiModel(weatherLarge.temp.max)
        val dayMinTemp = convertTempToUiModel(weatherLarge.temp.min)
        val highAndLowTemp = Pair(dayMaxTemp, dayMinTemp)

        return DailyForecastItemUiState(
            day = day,
            forecastUrlImage = forecastUrlImage,
            highAndLowTemp = highAndLowTemp
        )
    }

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

    /** Search Section Converter*/

    fun locationEntityToSearchResultItemUiState(rawList: List<String>): List<SearchResultItemUiState> {
        val searchResultListUiState = mutableListOf<SearchResultItemUiState>()

        rawList.forEach {
            searchResultListUiState.add(
                SearchResultItemUiState(name = it)
            )
        }

        return searchResultListUiState
    }
}