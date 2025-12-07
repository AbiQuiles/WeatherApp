package com.example.weather

import android.app.Application
import com.example.weather.database.WeatherDatabaseInitializer
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltAndroidApp
class WeatherApplication: Application() {

    @Inject
    lateinit var dataBaseInitializer: WeatherDatabaseInitializer

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.Default).launch {
            dataBaseInitializer.initializeDatabase()
        }
    }
}