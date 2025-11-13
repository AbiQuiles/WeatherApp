package com.example.weather.repository

import com.example.weather.models.ui.search.SavedItemUiState
import com.example.weather.models.ui.search.SearchItemUiState
import com.example.weather.models.ui.weather.CurrentWeatherUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationSupportedRepository: LocationSupportedRepository,
    private val locationSavedRepository: LocationSavedRepository
) {
    fun getAllLocationsSaved(): Flow<List<SavedItemUiState>> = locationSavedRepository.getAllLocations()

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

    suspend fun updateLocationSavedById(saveItemId: UUID, currentWeatherUiState: CurrentWeatherUiState): Boolean =
        withContext(Dispatchers.IO) {
             locationSavedRepository.updateLocationById(
                 saveItemId = saveItemId,
                 currentWeatherUiState = currentWeatherUiState
             )
        }

    suspend fun deleteLocationSaved(location: String): Boolean =
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