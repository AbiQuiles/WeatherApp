package com.example.weather.screens.main

import androidx.lifecycle.ViewModel
import com.example.weather.data.DataOrException
import com.example.weather.models.Weather
import com.example.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherMainViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel() {

    suspend fun getWeatherData(city: String): DataOrException<Weather, Boolean, Exception> {
        return repository.getWeather(cityQuery = city)
    }

/*
    val data: MutableState<DataOrException<Weather, Boolean, Exception>> =
        mutableStateOf(DataOrException(null, true, null))

    init {
        loadWeather()
    }

    private fun loadWeather(city: String = "Seattle") = getWeather(city)

    private fun getWeather(city: String) {
        viewModelScope.launch {

            if (city.isEmpty()){
                return@launch
            }

            data.value.loading = true
            data.value = repository.getWeather(cityQuery = city)

            val emptyCheck = data.value.data.toString().isNotEmpty()
            if (emptyCheck) {
                data.value.loading = false
            }

            Log.d("WeatherMainViewModel", "GET - Weather: ${data.value.data}")
        }
    }
    */
}