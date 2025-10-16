package com.example.weather.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weather.models.data.location.LocationSupportedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationSupportedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locations: List<LocationSupportedEntity>)

    @Query(value = "SELECT * from location_supported_tbl")
    fun getAll(): Flow<List<LocationSupportedEntity>>

    @Query(value = "SELECT * from location_supported_tbl where id = :id")
    suspend fun getById(id: String): LocationSupportedEntity

    @Query(value = "SELECT * from location_supported_tbl where location_name = :name")
    suspend fun getByName(name: String): LocationSupportedEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: LocationSupportedEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: LocationSupportedEntity)

    @Delete
    suspend fun delete(entity: LocationSupportedEntity)
}