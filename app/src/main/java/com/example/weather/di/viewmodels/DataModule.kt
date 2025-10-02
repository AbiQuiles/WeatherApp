package com.example.weather.di.viewmodels

import com.google.gson.Gson
import dagger.hilt.components.SingletonComponent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton

object DataModule {
    @Module
    @InstallIn(SingletonComponent::class)
    object DataModule {

        @Provides
        @Singleton
        fun provideGson(): Gson {
            return Gson()
        }
    }
}