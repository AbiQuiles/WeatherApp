package com.example.weather.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.models.ModelConverter
import com.example.weather.models.ui.WeatherMainUiState
import com.example.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherMainViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val converter: ModelConverter
): ViewModel() {
    private val _weatherUiState = MutableStateFlow<WeatherMainUiState?>(null)
    val weatherUiState: StateFlow<WeatherMainUiState?> = _weatherUiState.asStateFlow()

    init {
        viewModelScope.launch {
            _weatherUiState.value =
                repository.getWeather(cityQuery = "Orlando").data?.let { entity ->
                    converter.weatherEntityToMainUiState(
                        weatherEntity = entity
                    )
                }
        }
    }

    suspend fun getWeather(city: String): StateFlow<WeatherMainUiState?> {
        _weatherUiState.value =
            repository.getWeather(cityQuery = city).data?.let { entity ->
                converter.weatherEntityToMainUiState(
                    weatherEntity = entity
                )
            }

        return weatherUiState
    }
}