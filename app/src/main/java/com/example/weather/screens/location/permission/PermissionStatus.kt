package com.example.weather.screens.location.permission

sealed class PermissionStatus {
    object Granted : PermissionStatus()
    object Denied : PermissionStatus()
    object Undetermined : PermissionStatus()
}