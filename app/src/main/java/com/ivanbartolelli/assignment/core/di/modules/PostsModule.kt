package com.ivanbartolelli.assignment.core.di.modules

import com.ivanbartolelli.assignment.features.posts.data.datasources.local.database.PostsDatabase
import com.ivanbartolelli.assignment.features.posts.data.datasources.remote.mediators.PostsRemoteMediator
import com.ivanbartolelli.assignment.features.posts.data.datasources.remote.services.PostService
import com.ivanbartolelli.assignment.features.posts.data.repos.PostsRepo
import com.ivanbartolelli.assignment.features.posts.data.repos.PostsRepoImpl
import com.ivanbartolelli.assignment.features.posts.domain.interactor.PostsInteractor
import com.ivanbartolelli.assignment.features.posts.domain.interactor.PostsInteractorImpl
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
    fun providePostsRepo(database: PostsDatabase): PostsRepo {
        return PostsRepoImpl(database)
    }

    @Singleton
    @Provides
    fun providePostsRemoteMediator(
        service: PostService,
        database: PostsDatabase
    ): PostsRemoteMediator {
        return PostsRemoteMediator(service, database)
    }

    @Singleton
    @Provides
    fun providePostsInteractor(
        postsRemoteMediator: PostsRemoteMediator,
        postsRepo: PostsRepo
    ): PostsInteractor {
        return PostsInteractorImpl(postsRemoteMediator, postsRepo)
    }
}