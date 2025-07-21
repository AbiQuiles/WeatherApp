package com.example.weather.di

import com.example.weather.models.ModelConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@InstallIn(ViewModelComponent::class)
@Module
object WeatherMainViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideModelConverter() : ModelConverter = ModelConverter()
}