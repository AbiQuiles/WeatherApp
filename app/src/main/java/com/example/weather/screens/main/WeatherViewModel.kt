package com.example.weather.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.models.converters.WeatherModelsConverter
import com.example.weather.models.ui.weather.CurrentWeatherUiState
import com.example.weather.models.ui.weather.DailyForecastItemUiState
import com.example.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val converter: WeatherModelsConverter
): ViewModel() {
    private val _currentWeatherUiState = MutableStateFlow<CurrentWeatherUiState?>(null)
    val currentWeatherUiState: StateFlow<CurrentWeatherUiState?> = _currentWeatherUiState.asStateFlow()

    private val _dailyForecastItemUiState = MutableStateFlow<List<DailyForecastItemUiState>>(emptyList())
    val dailyForecastItemUiState: StateFlow<List<DailyForecastItemUiState?>> = _dailyForecastItemUiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getWeather(cityQuery = "Orlando").data?.let { entity ->
                _currentWeatherUiState.value = converter.weatherEntityToCurrentWeatherUiState(
                    weatherEntity = entity
                )

                _dailyForecastItemUiState.value = entity.list.slice(1..6).map { weatherLarge ->
                    converter.weatherEntityToDailyIForecastItemUiState(
                        weatherLarge = weatherLarge
                    )
                }
            }
        }

    }

    suspend fun getWeather(city: String): StateFlow<CurrentWeatherUiState?> {
        _currentWeatherUiState.value =
            repository.getWeather(cityQuery = city).data?.let { entity ->
                converter.weatherEntityToCurrentWeatherUiState(
                    weatherEntity = entity
                )
            }

        return currentWeatherUiState
    }
}