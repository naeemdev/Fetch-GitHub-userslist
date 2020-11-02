package com.naeem.fetchgithubuserslist_mvvm_room.di

import com.naeem.fetchgithubuserslist_mvvm_room.data.remote.api.RetofitService

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class ApiModule {

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        okHttpClientBuilder.addInterceptor(loggingInterceptor)
        return okHttpClientBuilder.build()
    }


    @Singleton
    @Provides
    fun provideRetrofitService(): RetofitService = Retrofit.Builder()
        .baseUrl(RetofitService.FOODIUM_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(providesOkHttpClient())
        .build()
        .create(RetofitService::class.java)
}
