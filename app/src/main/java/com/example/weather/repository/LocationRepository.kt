package com.example.weather.repository

import com.example.weather.models.ui.search.SavedItemUiState
import com.example.weather.models.ui.search.SearchItemUiState
import com.example.weather.models.ui.weather.CurrentWeatherUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationSupportedRepository: LocationSupportedRepository,
    private val locationSavedRepository: LocationSavedRepository
) {
    fun getAllLocations(): Flow<List<SavedItemUiState>> = locationSavedRepository.getAllLocations()

    fun getAllLocationSupported(): Flow<List<SearchItemUiState>> = locationSupportedRepository.getAllLocationSupported()

    suspend fun saveLocation(weatherUiState: CurrentWeatherUiState): Boolean =
        withContext(Dispatchers.IO) {
            val saveSuccess = locationSavedRepository.insertLocation(weatherUiState)

            if (saveSuccess) {
                locationSupportedRepository.updateSaveTag(location = weatherUiState.city)
                true
            } else {
                false
            }
        }

    suspend fun deleteLocation(location: String): Boolean =
        withContext(Dispatchers.IO) {
            val deleteSuccess = locationSavedRepository.deleteLocation(location)

            if (deleteSuccess) {
                locationSupportedRepository.updateSaveTag(location)
                true
            } else {
                false
            }
        }
}