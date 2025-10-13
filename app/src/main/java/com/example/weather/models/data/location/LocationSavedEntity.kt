package com.example.weather.models.data.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "location_saved_tbl")
data class LocationSavedEntity(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "location_name")
    val name: String,
    @ColumnInfo(name = "maxTemp")
    val maxTemp: String,
    @ColumnInfo(name = "minTemp")
    val minTemp: String
)
