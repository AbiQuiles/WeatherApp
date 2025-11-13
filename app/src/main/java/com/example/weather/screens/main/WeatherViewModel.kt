package com.example.weather.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.models.ui.weather.CurrentWeatherUiState
import com.example.weather.models.ui.weather.DailyForecastItemUiState
import com.example.weather.repository.LocationRepository
import com.example.weather.repository.LocationUserRepository
import com.example.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationUserRepository: LocationUserRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _currentWeatherUiState: MutableStateFlow<CurrentWeatherUiState?> = MutableStateFlow(null)
    val currentWeatherUiState: StateFlow<CurrentWeatherUiState?> = _currentWeatherUiState.asStateFlow()

    private val _dailyForecastItemUiState: MutableStateFlow<List<DailyForecastItemUiState>> = MutableStateFlow(emptyList())
    val dailyForecastItemUiState: StateFlow<List<DailyForecastItemUiState?>> = _dailyForecastItemUiState.asStateFlow()

    init {
        viewModelScope.launch {
            locationUserRepository.cityName.collect { name ->
                if (name != null) {
                    getSelectedWeather(location = name)
                }
            }
        }
    }

    fun getSelectedWeather(location: String) = viewModelScope.launch {
        weatherRepository.getWeather(location).collect { weatherScreenUiState ->
            _currentWeatherUiState.value = weatherScreenUiState.currentWeather
            _dailyForecastItemUiState.value = weatherScreenUiState.weekWeekForecast
        }
    }

    fun saveLocationByName(onSaveSuccess: (Boolean) -> Unit) {
        val currentWeatherState: CurrentWeatherUiState? = _currentWeatherUiState.value

        if (currentWeatherState == null) {
            onSaveSuccess(false)
            return
        }

        viewModelScope.launch {
            val saveSuccess = locationRepository.saveLocation(
                weatherUiState = currentWeatherState
            )

            onSaveSuccess(saveSuccess)
        }
    }
}