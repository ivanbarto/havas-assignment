package com.ivanbartolelli.kotlinrepos.core.di.modules

import android.app.Application
import androidx.room.Room
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.RepositoriesDatabase
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dto.RepositoriesConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): RepositoriesDatabase {
        return Room.databaseBuilder(application, RepositoriesDatabase::class.java, RepositoriesConstants.DATABASE_NAME).build()
    }
}