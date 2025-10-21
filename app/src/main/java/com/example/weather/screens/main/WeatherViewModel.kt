package com.example.weather.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.models.converters.WeatherModelsConverter
import com.example.weather.models.data.location.LocationSavedEntity
import com.example.weather.models.data.weather.WeatherDto
import com.example.weather.models.ui.weather.CurrentWeatherUiState
import com.example.weather.models.ui.weather.DailyForecastItemUiState
import com.example.weather.repository.LocationSavedRepository
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
    private val savedRepository: LocationSavedRepository,
    private val converter: WeatherModelsConverter
): ViewModel() {
    private val _cachedWeatherDTO: MutableStateFlow<WeatherDto?> = MutableStateFlow(null)

    private val _currentWeatherUiState: MutableStateFlow<CurrentWeatherUiState?> = MutableStateFlow(null)
    val currentWeatherUiState: StateFlow<CurrentWeatherUiState?> = _currentWeatherUiState.asStateFlow()

    private val _dailyForecastItemUiState: MutableStateFlow<List<DailyForecastItemUiState>> = MutableStateFlow(emptyList())
    val dailyForecastItemUiState: StateFlow<List<DailyForecastItemUiState?>> = _dailyForecastItemUiState.asStateFlow()

    //TODO: ViewModel should not the aware of the Entity
    //TODO: Find a way to get the initial location from the device location or a user input
    init {
        getSelectedWeather(location = "Orlando")
    }

    fun getSelectedWeather(location: String) = viewModelScope.launch {
        weatherRepository.getWeather(cityQuery = location).data?.let { weatherDto ->
            _cachedWeatherDTO.value = weatherDto

            _currentWeatherUiState.value = converter.weatherDtoToCurrentWeatherUiState(
                weatherDto = weatherDto
            )

            _dailyForecastItemUiState.value = weatherDto.list.slice(1..6).map { weatherLarge ->
                converter.weatherLargeToDailyIForecastItemUiState(
                    weatherLarge = weatherLarge
                )
            }
        }
    }

    fun saveLocationByName(onSave: Boolean) {
        viewModelScope.launch {
            val currentWeatherState = _currentWeatherUiState.value

            if (currentWeatherState != null && onSave) {
                savedRepository.insertLocation(
                    entity = LocationSavedEntity(
                        name = currentWeatherState.city ,
                        descriptionTemp = currentWeatherState.tempDescription,
                        currentTemp = currentWeatherState.currentTemp,
                        maxTemp = currentWeatherState.highAndLowTemp.second,
                        minTemp = currentWeatherState.highAndLowTemp.first
                    )
                )
            }
        }
    }
}