package com.example.weather.repository

import android.content.Context
import android.util.Log
import com.example.weather.models.data.location.LocationEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import java.io.InputStreamReader
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val context: Context,
    private val gson: Gson
) {
    val _mockLocations = listOf(
        "Orlando",
        "Altamonte Springs",
        "Wekiva",
        "Apopka",
        "Maitland",
        "Winter Park",
        "Maimi",
        "Clair Mount",
        "Ocoee",
    )

    private val INPUT_FILE_NAME = "locations_list.json"
    private val TAG = "LocationRepository"

/*    suspend fun getAllLocationNamesWithGson(): List<String> {
        val namesList = mutableListOf<String>()

        withContext(Dispatchers.IO) {
            try {
                // 2. Open the asset file and create an InputStreamReader
                context.assets.open(INPUT_FILE_NAME).use { inputStream ->
                    val inputStreamReader = InputStreamReader(inputStream, Charsets.UTF_8)

                    // 3. Define the target type: a List of Location objects
                    val listType = object : TypeToken<List<LocationDto>>() {}.type

                    // 4. Deserialize the entire JSON file into a List<Location>
                    val locations: List<LocationDto> = gson.fromJson(inputStreamReader, listType)

                    // 5. Extract just the names using Kotlin's 'map' function
                    namesList.addAll(locations.map { it.name })
                }
            } catch (ioE: IOException) {
                Log.e(TAG, "IOException while parsing asset file: ${ioE.message}", ioE)
            } catch (e: Exception) {
                Log.e(TAG, "General error during name extraction: ${e.message}", e)
            }
        }

        return namesList
    }*/
}
