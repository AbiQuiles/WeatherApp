package com.example.weather.di.viewmodels

import com.example.weather.models.converters.WeatherModelsConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
object WeatherViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideModelConverter() : WeatherModelsConverter = WeatherModelsConverter()
}