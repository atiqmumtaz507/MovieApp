package com.atiq.neugelb.di

import com.atiq.neugelb.data.remote.MoviesApi
import com.atiq.neugelb.data.repository.MoviesRepositoryImpl
import com.atiq.neugelb.domain.repositories.MoviesRepository
import com.atiq.neugelb.utils.Constant
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetWorkModule {
    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }

        return Retrofit
            .Builder()
            .client(client)
            .baseUrl(Constant.API_BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Singleton
    @Provides
    fun provideMoviesApi(retrofit: Retrofit): MoviesApi {
        return retrofit.create(MoviesApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMovieRepo (api: MoviesApi): MoviesRepository {
        return MoviesRepositoryImpl(api)
    }
}