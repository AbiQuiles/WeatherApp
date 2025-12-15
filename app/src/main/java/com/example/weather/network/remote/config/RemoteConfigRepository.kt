package com.example.weather.network.remote.config

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigRepository @Inject constructor() {
    private val WEATHER_API_KEY: String = "open_weather_api_key"
    private val LOCATION_FILE_URL: String = "location_file_url"
    private val LOCATION_FILE_VERSION: String = "location_file_version"

    private val _weatherApiKey: MutableStateFlow<String?> = MutableStateFlow(null)
    val weatherApiKey: StateFlow<String?> = _weatherApiKey.asStateFlow()

    suspend fun fetchConfigs(): RemoteConfigRepository {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings: FirebaseRemoteConfigSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1000
        }

        remoteConfig.setConfigSettingsAsync(configSettings)
        Log.d("RemoteConfigRepository", "RemoteConfig $remoteConfig")
        //TODO: Set default values (optional but recommended)
        /*val defaults = mapOf(
            WEATHER_API_KEY to "", // Default to empty string
            LOCATION_FILE_URL to "",
            LOCATION_FILE_VERSION to "0.0.0"
        )
        remoteConfig.setDefaultsAsync(defaults)*/
        //remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        try {
            Log.d("RemoteConfigRepository", "Attempting to fetch and activate Remote Config...")
            val wasSuccessful = remoteConfig.fetchAndActivate().await()
            if (wasSuccessful) {
                Log.d("RemoteConfigManager", "Fetch and activate call was SUCCESSFUL.")
            } else {
                Log.w("RemoteConfigManager", "Fetch and activate call was NOT successful. Using cached or default values.")
            }

            _weatherApiKey.value = remoteConfig.getString(WEATHER_API_KEY)
            Log.d("RemoteConfigManager", "--- Fetched Values ---")
            Log.d("RemoteConfigManager", "API Key: ${weatherApiKey.value}")

            return this
        } catch (e: Exception) {
            Log.e("RemoteConfigRepository", "Error: ${e.message}")

            return this
        }
    }

    /*   private suspend fun downloadAndProcessFile(fileUrl: String) {
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