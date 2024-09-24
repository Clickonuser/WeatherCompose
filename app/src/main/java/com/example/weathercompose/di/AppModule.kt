package com.example.weathercompose.di

import android.content.Context
import com.example.weathercompose.repository.WeatherRepository
import com.example.weathercompose.repository.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideWeatherRepository(@ApplicationContext context: Context): WeatherRepository {
        return WeatherRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideContext(application: HiltApplication): Context {
        return application.applicationContext
    }
}