package com.naeem.fetchgithubuserslist_mvvm_room.di

import android.app.Application
import com.naeem.fetchgithubuserslist_mvvm_room.data.local.GithubUserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application) = GithubUserDatabase.getInstance(application)

    @Singleton
    @Provides
    fun providePostsDao(database: GithubUserDatabase) = database.getPostsDao()
}
