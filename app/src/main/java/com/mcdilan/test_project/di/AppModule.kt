package com.mcdilan.test_project.di

import com.mcdilan.test_project.network.WebSocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideWebSocketClient(): WebSocketClient = WebSocketClient()
}