package com.example.weather.repository

import com.example.weather.models.ui.weather.CurrentWeatherUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationSupportedRepository: LocationSupportedRepository,
    private val locationSavedRepository: LocationSavedRepository
) {
    suspend fun saveLocation(weatherUiState: CurrentWeatherUiState) =
        withContext(Dispatchers.IO) {
            val saveSuccess = locationSavedRepository.insertLocation(weatherUiState)

            if (saveSuccess) {
                locationSupportedRepository.updateSaveTag(location = weatherUiState.city)
            }
        }
}