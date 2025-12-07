package com.example.weather.network.remote.config

data class RemoteConfigModel(
    val weatherApiKey: String,
    val locationFileUrl: String,
    val locationFileVersion: String
) {
    companion object Companion {
        /**
         * Provides a default, "empty" instance of the configuration.
         * Used as a fallback to prevent app crashes when the fetch fails.
         */
        fun fallback() = RemoteConfigModel(
            weatherApiKey = "",
            locationFileUrl = "",
            locationFileVersion = "0.0.0"
        )
    }
}
