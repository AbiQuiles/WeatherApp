package com.example.weather.screens.location.permission

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.repository.LocationUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationPermissionPromptViewModel @Inject constructor(
    private val locationUserRepository: LocationUserRepository
): ViewModel() {

    // The UI will call this function after it confirms permission has been granted.
    fun onPermissionGranted(activity: Activity) {
        viewModelScope.launch {
            locationUserRepository.fetchUserLocation(activity)
        }
    }
}