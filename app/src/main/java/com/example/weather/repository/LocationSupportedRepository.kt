package com.example.weather.repository

import com.example.weather.database.LocationSupportedDao
import com.example.weather.models.data.location.LocationSupportedEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LocationSupportedRepository @Inject constructor(
    private val locationSupportedDao: LocationSupportedDao
) {

    fun getAllLocationSupported(): Flow<List<LocationSupportedEntity>> =
        locationSupportedDao.getAll()
            .flowOn(Dispatchers.IO)
            .conflate()

}
