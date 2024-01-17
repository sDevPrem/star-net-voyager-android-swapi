package com.example.starnetvoyager.di

import android.content.Context
import com.example.starnetvoyager.data.local.StarWarsDataStore
import com.example.starnetvoyager.data.network.StarWarsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesBaseUrl(): String {
        return "https://swapi.dev/api/"
    }

    @Singleton
    @Provides
    fun providesConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun providesRetrofit(
        baseUrl: String,
        converterFactory: Converter.Factory,
    ): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)

        return retrofit.build()
    }

    @Singleton
    @Provides
    fun providesStarWarsDataSource(retrofit: Retrofit): StarWarsDataSource =
        retrofit.create(StarWarsDataSource::class.java)

    @Singleton
    @Provides
    fun providesStarWarsDataStore(@ApplicationContext context: Context) =
        StarWarsDataStore.createDatabase(context)

}