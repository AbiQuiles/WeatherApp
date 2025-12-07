package com.example.weather.network.remote.config

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface RemoteConfig {
    var weatherApiKey: String
}

class RemoteConfigManager @Inject constructor(
    override var weatherApiKey: String = ""
): RemoteConfig {
    private val WEATHER_API_KEY: String = "open_weather_api_key"
    private val LOCATION_FILE_URL: String = "location_file_url"
    private val LOCATION_FILE_VERSION: String = "location_file_version"

    fun fetchConfigs(): RemoteConfigManager {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings: FirebaseRemoteConfigSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1000
        }

        remoteConfig.setConfigSettingsAsync(configSettings)
        Log.d("RemoteConfig", "RemoteConfig $remoteConfig")
        //TODO: Set default values (optional but recommended)
        //remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        //return remoteConfig
        try {
            println("Rez fetching...")
            remoteConfig.fetchAndActivate()

            weatherApiKey = remoteConfig.getString(WEATHER_API_KEY)
            println("Rez Success key $weatherApiKey")
            Log.d("RemoteConfig", "RemoteConfig properties fetch successful!")
            //RemoteConfigObj.apiKey = apiKey

            return this
        } catch (e: Exception) {
            Log.e("RemoteConfig", "Error: ${e.message}")

            println("Rez Failed $")
            return this
        }
    }

    suspend fun initializeRemoteConfigG(): RemoteConfigModel {
        return try {
            Log.d("RemoteConfigManager", "Attempting to fetch and activate Remote Config...")
            val remoteConfig = Firebase.remoteConfig

            // 1. Set configuration settings. Use a lower interval for debugging if needed.
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0 // 1 hour for production
            }
            remoteConfig.setConfigSettingsAsync(configSettings)

            // 2. Set default values. This is crucial for the first launch or when offline.
            val defaults = mapOf(
                WEATHER_API_KEY to "", // Default to empty string
                LOCATION_FILE_URL to "",
                LOCATION_FILE_VERSION to "0.0.0"
            )
            remoteConfig.setDefaultsAsync(defaults)

            // 3. Fetch data from the network and activate it.
            val wasSuccessful = remoteConfig.fetchAndActivate().await()
            if (wasSuccessful) {
                Log.d("RemoteConfigManager", "Fetch and activate call was SUCCESSFUL.")
            } else {
                Log.w("RemoteConfigManager", "Fetch and activate call was NOT successful. Using cached or default values.")
            }

            // 4. Retrieve the values using the keys.
            val apiKey = remoteConfig.getString(WEATHER_API_KEY)
            val fileUrl = remoteConfig.getString(LOCATION_FILE_URL)
            val fileVersion = remoteConfig.getString(LOCATION_FILE_VERSION)

            // 5. Add DETAILED logging to diagnose the issue.
            Log.d("RemoteConfigManager", "--- Fetched Values ---")
            Log.d("RemoteConfigManager", "API Key: '$apiKey'")
            Log.d("RemoteConfigManager", "File URL: '$fileUrl'")
            Log.d("RemoteConfigManager", "File Version: '$fileVersion'")
            Log.d("RemoteConfigManager", "--------------------")

            if (apiKey.isEmpty()) {
                Log.e("RemoteConfigManager", "CRITICAL: The fetched API key is EMPTY. Check your Firebase console key name and published values.")
            }

            // 6. Create and return the immutable data class with the REAL values.
            RemoteConfigModel(
                weatherApiKey = apiKey,
                locationFileUrl = fileUrl,
                locationFileVersion = fileVersion
            )
        } catch (e: Exception) {
            Log.e("RemoteConfigManager", "An exception occurred while fetching remote config. Returning fallback.", e)
            // 7. On any failure, return a safe, default object to prevent crashes.
            RemoteConfigModel.fallback()
        }
    }

private suspend fun getRemoteConfig(remoteConfig: FirebaseRemoteConfig): RemoteConfigModel {
        try {
            println("Rez here")
            remoteConfig.fetchAndActivate().await()

            val apiKey = remoteConfig.getString(WEATHER_API_KEY)
            //val fileUrl = remoteConfig.getString(LOCATION_FILE_URL)
            //val fileVersion = remoteConfig.getString(LOCATION_FILE_VERSION)

            println("Rez key $apiKey")
            Log.d("RemoteConfig", "RemoteConfig properties fetch successful!")

            return RemoteConfigModel(
                weatherApiKey = apiKey,
                locationFileUrl = "fileUrl",
                locationFileVersion = "fileVersion"
            )
        } catch (e: Exception) {
            Log.e("RemoteConfig", "Error: ${e.message}")
            return RemoteConfigModel(
                weatherApiKey = "",
                locationFileUrl = "",
                locationFileVersion = "0.0.0"
            )
        }
    }

    /*private suspend fun downloadAndProcessFile(fileUrl: String) {
        // Run file I/O on the IO dispatcher
        withContext(Dispatchers.IO) {
            try {
                val destinationFile = File(context.cacheDir, "locations_list.json")

                // Download
                URL(fileUrl).openStream().use { input ->
                    destinationFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                Log.d("AppSetupUseCase", "File downloaded to ${destinationFile.path}")

                // Process (Here you would parse the JSON and save to Room)
                // val jsonString = destinationFile.readText()
                // val dtos = parseJsonToDtos(jsonString)
                // yourDataRepository.insertEntitiesFromDtos(dtos)
                Log.d("AppSetupUseCase", "File processing would happen here.")

                // Delete
                destinationFile.delete()
                Log.d("AppSetupUseCase", "Cache file deleted.")

            } catch (e: Exception) {
                Log.e("AppSetupUseCase", "Failed to download or process file", e)
                // Decide how to handle this error. Maybe the app can continue without this data.
            }
        }
    }*/
}