package com.example.weather.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weather.models.data.location.LocationSavedEntity
import com.example.weather.models.data.location.LocationSupportedEntity

@Database(
    entities = [
        LocationSupportedEntity::class,
        LocationSavedEntity:: class
    ],
    version = 1,
    exportSchema = false
)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun locationSupportedDao(): LocationSupportedDao
    abstract fun locationSavedDao(): LocationSavedDao
}