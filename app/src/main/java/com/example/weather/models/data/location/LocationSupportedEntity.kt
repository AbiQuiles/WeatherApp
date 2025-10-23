package com.example.weather.models.data.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "location_supported_tbl")
data class LocationSupportedEntity(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "location_name")
    val name: String,
    @ColumnInfo(name = "location_save", defaultValue = "0")
    val saveTag: Boolean = false
)