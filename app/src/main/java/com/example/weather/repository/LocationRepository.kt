package com.example.weather.repository

import android.content.Context
import android.util.Log
import com.example.weather.models.data.location.LocationDto
import com.example.weather.models.data.location.LocationSavedEntity
import com.example.weather.models.data.location.LocationSupportedEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val context: Context,
    private val gson: Gson
) {
    val _mockLocationSaved: List<LocationSavedEntity> = listOf(
        LocationSavedEntity(
            name = "Orlando",
            maxTemp = "-65",
            minTemp = "102+"
        ),
        LocationSavedEntity(name = "Altamonte Springs",
            maxTemp = "-65",
            minTemp = "102+"
        ),
        LocationSavedEntity(name = "Wekiva Springs",
            maxTemp = "-65",
            minTemp = "102+"
        ),
        LocationSavedEntity(name = "Apopka",
            maxTemp = "-65",
            minTemp = "102+"
        ),
        LocationSavedEntity(name = "Maitland",
            maxTemp = "-65",
            minTemp = "102+"
        ),
        LocationSavedEntity(name = "Winter Park",
            maxTemp = "-65",
            minTemp = "102+"
        ),
        LocationSavedEntity(name = "Miami",
            maxTemp = "-65",
            minTemp = "102+"
        ),
        LocationSavedEntity(name = "Clermont",
            maxTemp = "-65",
            minTemp = "102+"
        ),
        LocationSavedEntity(name = "Ocoee",
            maxTemp = "-65",
            minTemp = "102+"
        ),
    )
    val _mockLocationsSupported: List<LocationSupportedEntity> = listOf(
        LocationSupportedEntity(name = "Orlando"),
        LocationSupportedEntity(name = "Altamonte Springs"),
        LocationSupportedEntity(name = "Wekiva Springs"),
        LocationSupportedEntity(name = "Apopka"),
        LocationSupportedEntity(name = "Maitland"),
        LocationSupportedEntity(name = "Winter Park"),
        LocationSupportedEntity(name = "Miami"),
        LocationSupportedEntity(name = "Clermont"),
        LocationSupportedEntity(name = "Ocoee"),
    )

    private val INPUT_FILE_NAME = "locations_list.json"
    private val TAG = "LocationRepository"

    suspend fun getAllLocationNamesWithGson(): List<LocationDto> {
        val namesList: MutableList<LocationDto> = mutableListOf()

        withContext(Dispatchers.IO) {
            try {
                context.assets.open(INPUT_FILE_NAME).use { inputStream ->
                    val inputStreamReader = InputStreamReader(inputStream, Charsets.UTF_8)

                    // 3. Define the target type: a List of Location objects
                    val listType = object : TypeToken<List<LocationDto>>() {}.type

                    // 4. Deserialize the entire JSON file into a List<Location>
                    val locations: List<LocationDto> = gson.fromJson(inputStreamReader, listType)

                    namesList.addAll(locations.map { LocationDto(name = it.name) })
                }
            } catch (ioE: IOException) {
                Log.e(TAG, "IOException while parsing asset file: ${ioE.message}", ioE)
            } catch (e: Exception) {
                Log.e(TAG, "General error during name extraction: ${e.message}", e)
            }
        }

        return namesList
    }
}
