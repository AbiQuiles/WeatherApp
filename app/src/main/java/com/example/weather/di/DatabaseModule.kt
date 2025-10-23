package com.example.weather.di

import android.content.Context
import androidx.room.Room
import com.example.weather.database.LocationSavedDao
import com.example.weather.database.LocationSupportedDao
import com.example.weather.database.WeatherDataBase
import com.example.weather.database.WeatherDatabaseInitializer
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideLocationSupportedDao(weatherAppDataBase: WeatherDataBase): LocationSupportedDao =
        weatherAppDataBase.locationSupportedDao()

    @Singleton
    @Provides
    fun provideLocationSavedDao(weatherAppDataBase: WeatherDataBase): LocationSavedDao =
        weatherAppDataBase.locationSavedDao()

    @Singleton
    @Provides
    fun provideWeatherAppDatabaseInitializer(
        @ApplicationContext appContext: Context,
        gson: Gson,
        locationSupportedDao: LocationSupportedDao
    ): WeatherDatabaseInitializer =
        WeatherDatabaseInitializer(
            context =  appContext,
            gson = gson,
            locationSupportedDao = locationSupportedDao
        )

    @Singleton
    @Provides
    fun provideWeatherAppDatabase(@ApplicationContext appContext: Context): WeatherDataBase =
        Room.databaseBuilder(
            context = appContext,
            klass = WeatherDataBase::class.java,
            name = "weather_db"
        ).build()

}