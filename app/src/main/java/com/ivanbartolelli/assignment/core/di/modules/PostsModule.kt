package com.ivanbartolelli.assignment.core.di.modules

import com.ivanbartolelli.assignment.features.repositories.data.datasources.local.database.PostsDatabase
import com.ivanbartolelli.assignment.features.repositories.data.datasources.remote.services.PostService
import com.ivanbartolelli.assignment.features.repositories.data.repos.PostsRepo
import com.ivanbartolelli.assignment.features.repositories.data.repos.PostsRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class PostsModule {

    @Singleton
    @Provides
    fun providePostsRepo(service: PostService, database: PostsDatabase): PostsRepo {
        return PostsRepoImpl(service, database)
    }
}