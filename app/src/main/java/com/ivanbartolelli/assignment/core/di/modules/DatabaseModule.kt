package com.ivanbartolelli.assignment.core.di.modules

import android.app.Application
import androidx.room.Room
import com.ivanbartolelli.assignment.features.posts.data.datasources.local.database.PostsDatabase
import com.ivanbartolelli.assignment.features.posts.data.datasources.local.database.DatabaseConstants.DATABASE_NAME
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
    fun provideDatabase(application: Application): PostsDatabase {
        return Room.databaseBuilder(application, PostsDatabase::class.java, DATABASE_NAME).build()
    }
}