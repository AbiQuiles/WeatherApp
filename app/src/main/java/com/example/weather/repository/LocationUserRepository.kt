package com.example.weather.repository

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationUserRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _cityName: MutableStateFlow<String?> = MutableStateFlow(null)
    val cityName: StateFlow<String?> = _cityName.asStateFlow()

    @SuppressLint("MissingPermission")
    suspend fun fetchUserLocation(activity: Activity) {
        withContext(Dispatchers.IO) {
            try {
                val location:Location? = getCurrentLocation(activity)

                if (location != null) {
                    val city: String? = getCityNameFromLocation(location)

                    _cityName.value = city
                } else {
                    _cityName.value = null
                }
            } catch (e: Exception) {
                Log.e("LocationUserRepository",e.message ?: "Error Msg: Error when fetching location")
                _cityName.value = null
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocation(activity: Activity): Location? {
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

        return try {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).await()
        } catch (e: Exception) {
            Log.e("LocationUserRepository",e.message ?: "Error Msg: Error when getting location from LocationService Provider")
            null
        }
    }

    // Extracted the geocoding logic
    private suspend fun getCityNameFromLocation(location: Location): String? {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                addresses?.firstOrNull()?.locality
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}