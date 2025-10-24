package com.example.weather.repository

import android.util.Log
import com.example.weather.database.LocationSupportedDao
import com.example.weather.models.mappers.toUiModel
import com.example.weather.models.ui.search.SearchItemUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationSupportedRepository @Inject constructor(
    private val locationSupportedDao: LocationSupportedDao
) {
    fun getAllLocationSupported(): Flow<List<SearchItemUiState>> = flow {
        try {
            locationSupportedDao.getAll()
                .flowOn(Dispatchers.IO)
                .distinctUntilChanged()
                .conflate()
                .collect { entities ->
                    val searchItemUiStates: List<SearchItemUiState> = entities.map { entity ->
                        entity.toUiModel()
                    }

                    emit(searchItemUiStates)
                }
        } catch (e: Exception) {
            Log.e("LocationSavedRepository", "Fetching saved locations error: ${e.message}", e)
            emit(emptyList())
        }
    }

    suspend fun updateSaveTag(location: String) =
        withContext(Dispatchers.IO) {
            try {
                val entity = locationSupportedDao.getByName(name = location)

                locationSupportedDao.update(
                    entity = entity.copy(saveTag = !entity.saveTag)
                )
            } catch (e: Exception) {
                Log.e("LocationSupportedRepo", "Failed to update save tag for $location", e)
            }
        }
}
