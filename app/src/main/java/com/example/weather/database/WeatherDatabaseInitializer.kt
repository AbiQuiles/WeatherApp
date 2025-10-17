package com.example.weather.database

import android.content.Context
import android.util.Log
import com.example.weather.models.data.location.LocationSupportedDto
import com.example.weather.models.data.location.LocationSupportedEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject

class WeatherDatabaseInitializer @Inject constructor(
    private val context: Context,
    private val gson: Gson,
    private val locationSupportedDao: LocationSupportedDao
) {
    private val INPUT_FILE_NAME = "locations_list.json"
    private val TAG = "WeatherDatabaseInitializer"

    suspend fun initializeDatabase() {
        //Check if the DB has been already populated
        if (locationSupportedDao.count() == 0) {
            val dtoList: List<LocationSupportedDto> = getLocationSupportedDTOs()
            if (dtoList.isNotEmpty()) {
                val entities: List<LocationSupportedEntity> = dtoList.map { dto -> LocationSupportedEntity(name = dto.name) }
                locationSupportedDao.insertAll(entities)
                Log.d(TAG, "Successfully populated ${entities.size} locations.")
            }
        } else {
            Log.d(TAG, "Database already populated. Skipping initialization.")
        }
    }

    private suspend fun getLocationSupportedDTOs(): List<LocationSupportedDto> {
        val namesList: MutableList<LocationSupportedDto> = mutableListOf()

        withContext(Dispatchers.IO) {
            try {
                context.assets.open(INPUT_FILE_NAME).use { inputStream ->
                    val inputStreamReader = InputStreamReader(inputStream, Charsets.UTF_8)

                    // Define the target type: a List of Location objects
                    val listType = object : TypeToken<List<LocationSupportedDto>>() {}.type

                    // Deserialize the entire JSON file into a List<LocationSupportedDto>
                    val locations: List<LocationSupportedDto> = gson.fromJson(inputStreamReader, listType)

                    namesList.addAll(locations.map {
                        LocationSupportedDto(name = it.name)
                    })
                }
            } catch (ioE: IOException) {
                Log.e(TAG, "IOException while parsing asset file: ${ioE.message}", ioE)
            } catch (e: Exception) {
                Log.e(TAG, "General error during name extraction: ${e.message}", e)
            }
        }

        // Filter out blank string results.
        // For a reason OpenWeather saved a location with a name property with an empty string
        return namesList.filter { it.name.isNotBlank() }
    }
}