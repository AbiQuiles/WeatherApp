package com.example.weather.models.data.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "location_saved_tbl")
data class LocationSavedEntity(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "location_primary", defaultValue = "0")
    val isPrimary: Boolean = false,
    @ColumnInfo(name = "location_name")
    val name: String,
    @ColumnInfo(name = "description_temp")
    val descriptionTemp: String,
    @ColumnInfo(name = "current_temp")
    val currentTemp: String,
    @ColumnInfo(name = "max_temp")
    val maxTemp: String,
    @ColumnInfo(name = "min_temp")
    val minTemp: String
)
