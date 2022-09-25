package com.mrntlu.bisu.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mrntlu.bisu.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideAuthentication(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideRealtimeDatabase(): FirebaseDatabase = Firebase.database

    @Provides
    @Singleton
    fun provideDatabaseReference(database: FirebaseDatabase): DatabaseReference = database.getReference(Constants.FIREBASE_DB_NAME)
}