package com.example.weather.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weather.models.data.location.LocationSavedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationSavedDao {

    @Query(value = "SELECT * from location_saved_tbl")
    fun getAll(): Flow<List<LocationSavedEntity>>

    @Query(value = "SELECT * from location_saved_tbl where id = :id")
    suspend fun getById(id: String): LocationSavedEntity

    @Query(value = "SELECT * from location_saved_tbl where location_name = :name")
    suspend fun getByName(name: String): LocationSavedEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: LocationSavedEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: LocationSavedEntity)

    @Delete
    suspend fun delete(entity: LocationSavedEntity)
}