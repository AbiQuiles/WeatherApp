package com.example.weather.repository

import com.example.weather.database.LocationSavedDao
import com.example.weather.models.data.location.LocationSavedEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationSavedRepository @Inject constructor(
    private val locationSavedDao: LocationSavedDao
) {
    val _mockLocationSaved: List<LocationSavedEntity> = listOf(
        LocationSavedEntity(
            name = "Orlando",
            descriptionTemp = "Sunny",
            currentTemp = "75",
            minTemp = "65",
            maxTemp = "102"
        ),
        LocationSavedEntity(
            name = "Altamonte Springs",
            descriptionTemp = "Rainy",
            currentTemp = "85",
            minTemp = "65",
            maxTemp = "102"
        ),
        LocationSavedEntity(
            name = "Wekiva Springs",
            descriptionTemp = "Mostly Sunny",
            currentTemp = "95",
            minTemp = "65",
            maxTemp = "102"
        ),
        LocationSavedEntity(
            name = "Apopka",
            descriptionTemp = "Cloudy ",
            currentTemp = "105",
            minTemp = "65",
            maxTemp = "102"
        ),
        LocationSavedEntity(
            name = "Maitland",
            descriptionTemp = "Sunny",
            currentTemp = "90",
            minTemp = "65",
            maxTemp = "102"
        ),
        LocationSavedEntity(
            name = "Winter Park",
            descriptionTemp = "Rainy",
            currentTemp = "70",
            minTemp = "65",
            maxTemp = "102"
        ),
        LocationSavedEntity(
            name = "Miami",
            descriptionTemp = "Sunny",
            currentTemp = "60",
            minTemp = "65",
            maxTemp = "102"
        ),
        LocationSavedEntity(
            name = "Clermont",
            descriptionTemp = "Cloudy",
            currentTemp = "75",
            minTemp = "65",
            maxTemp = "102"
        ),
        LocationSavedEntity(
            name = "Ocoee",
            descriptionTemp = "Most Sunny",
            currentTemp = "95",
            minTemp = "65",
            maxTemp = "102"
        ),
    )

    fun getAllLocations(): Flow<List<LocationSavedEntity>> = locationSavedDao.getAll()
    suspend fun insertLocation(entity: LocationSavedEntity) = locationSavedDao.insert(entity)
    suspend fun updateLocation(entity: LocationSavedEntity) = locationSavedDao.update(entity)
    suspend fun deleteLocation(entity: LocationSavedEntity) = locationSavedDao.delete(entity)
}