package com.mrntlu.bisu.di

import com.mrntlu.bisu.service.NewsApiService
import com.mrntlu.bisu.service.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance(): NewsApiService =
        RetrofitClient
            .getClient()
            .create(NewsApiService::class.java)
}