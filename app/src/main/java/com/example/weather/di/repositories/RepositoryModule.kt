package com.example.weather.di.repositories

import com.example.weather.database.LocationSavedDao
import com.example.weather.database.LocationSupportedDao
import com.example.weather.repository.LocationSavedRepository
import com.example.weather.repository.LocationSupportedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLocationSavedRepository(locationSavedDao: LocationSavedDao): LocationSavedRepository =
        LocationSavedRepository(
            locationSavedDao = locationSavedDao
        )


    @Provides
    @Singleton
    fun provideLocationSupportedRepository(locationSupportedDao: LocationSupportedDao): LocationSupportedRepository =
        LocationSupportedRepository(
            locationSupportedDao = locationSupportedDao
        )

}
