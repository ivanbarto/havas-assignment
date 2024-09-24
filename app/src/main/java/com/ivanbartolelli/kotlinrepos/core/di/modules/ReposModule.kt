package com.ivanbartolelli.kotlinrepos.core.di.modules

import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.PostsDatabase
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.services.PostService
import com.ivanbartolelli.kotlinrepos.features.repositories.data.repos.PostsRepo
import com.ivanbartolelli.kotlinrepos.features.repositories.data.repos.PostsRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ReposModule {

    @Singleton
    @Provides
    fun providePostsRepo(service: PostService, database: PostsDatabase): PostsRepo {
        return PostsRepoImpl(service, database)
    }
}