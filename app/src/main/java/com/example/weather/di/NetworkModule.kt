package com.example.weather.di

import com.example.weather.network.WeatherApi
import com.example.weather.network.remote.config.RemoteConfigManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    /*@Singleton
    @Provides
    fun provideRemoteConfigI(weatherRemoteConfig: WeatherRemoteConfig): RemoteConfig =
        weatherRemoteConfig.remoteConfig()*/

    @Singleton
    @Provides
    fun provideRemoteConfig(): RemoteConfigManager {
        return RemoteConfigManager().fetchConfigs()
    }


/*    @Provides
    @Singleton
    fun provideOkHttpClient(remoteConfigManager: RemoteConfigManager): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val apiKey = remoteConfigManager.weatherApiKet
                println("Rez Hilt ${apiKey}")
                val original = chain.request()
                val newUrl = original.url.newBuilder()
                    .addQueryParameter("appid", apiKey)
                    .build()

                val request = original
                    .newBuilder()
                    .url(newUrl)
                    .build()

                chain.proceed(request)
            }
            .build()*/

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit =
        Retrofit
            .Builder()
            .baseUrl("https://api.openweathermap.org/")
            //.client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Singleton
    @Provides
    fun providerWeatherApi(retrofit: Retrofit): WeatherApi =
        retrofit.create(WeatherApi::class.java)
}