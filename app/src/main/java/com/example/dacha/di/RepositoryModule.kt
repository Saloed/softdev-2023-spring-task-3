package com.example.dacha.di

import android.content.SharedPreferences
import com.example.dacha.data.repository.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun providePersonRepository(database: FirebaseDatabase): PersonRepository {
        return PersonRepositoryImpl(database)
    }

    @Singleton
    @Provides
    fun  provideProductsRepository(database: FirebaseDatabase, shPref: SharedPreferences): ProductRepository {
        return ProductRepositoryImpl(database, shPref)
    }

    @Singleton
    @Provides
    fun provideHomeRepository(database: FirebaseDatabase, shPref: SharedPreferences): HomeRepository {
        return HomeRepositoryImpl(database, shPref)
    }

    @Singleton
    @Provides
    fun provideDebtsRepository(database: FirebaseDatabase): DebtRepository {
        return DebtRepositoryImpl(database)
    }

    @Singleton
    @Provides
    fun provideDashboardRepository(database: FirebaseDatabase, storageReference: StorageReference): DashboardRepository {
        return DashboardRepositoryImpl(database, storageReference)
    }
}