package com.example.weather.repository

import android.util.Log
import com.example.weather.database.LocationSavedDao
import com.example.weather.models.data.location.LocationSavedEntity
import com.example.weather.models.mappers.toUiModel
import com.example.weather.models.ui.search.SavedItemUiState
import com.example.weather.models.ui.weather.CurrentWeatherUiState
import com.example.weather.util.data.cleanString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LocationSavedRepository @Inject constructor(
    private val locationSavedDao: LocationSavedDao
) {
    fun getAllLocations(): Flow<List<SavedItemUiState>> = flow {
        try {
            locationSavedDao.getAll()
                .flowOn(Dispatchers.IO)
                .conflate()
                .distinctUntilChanged()
                .collect { entities ->
                    val savedItemUiStates: List<SavedItemUiState> = entities.map { entity ->
                        entity.toUiModel()
                    }

                    emit(savedItemUiStates)
                }
        } catch (e: Exception) {
            Log.e("LocationSavedRepository", "Fetching saved locations error: ${e.message}", e)
            emit(emptyList())
        }
    }

    suspend fun insertLocation(uiModel: CurrentWeatherUiState): Boolean {
        return try {
            val entity = LocationSavedEntity(
                name = uiModel.city ,
                descriptionTemp = uiModel.tempDescription,
                currentTemp = cleanString(uiModel.currentTemp),
                maxTemp = cleanString(uiModel.highAndLowTemp.second),
                minTemp = cleanString(uiModel.highAndLowTemp.first)
            )

            locationSavedDao.insert(entity)
            true
        } catch (e: Exception) {
            Log.e("LocationSavedRepository", "Save locations attempt error: ${e.message}", e)
            false
        }
    }

    suspend fun updateLocation(entity: LocationSavedEntity) = locationSavedDao.update(entity)

    suspend fun deleteLocation(location: String): Boolean {
        return try {
            locationSavedDao.deleteByName(name = location)
            true
        } catch (e: Exception) {
            Log.e("LocationSavedRepository", "Delete locations attempt error: ${e.message}", e)
            false
        }
    }
}