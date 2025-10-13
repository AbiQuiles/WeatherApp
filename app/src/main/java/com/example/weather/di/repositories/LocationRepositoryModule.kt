package com.example.weather.di.repositories

import android.content.Context
import com.example.weather.repository.LocationRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLocationRepository(
        @ApplicationContext context: Context,
        gson: Gson
    ): LocationRepository {
        return LocationRepository(context, gson)
    }
}
