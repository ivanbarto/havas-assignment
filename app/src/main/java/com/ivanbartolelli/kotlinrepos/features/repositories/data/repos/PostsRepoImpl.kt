package com.ivanbartolelli.kotlinrepos.features.repositories.data.repos

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.PostsDatabase
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.utils.PostsConstants
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.mediators.PostRemoteMediator
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.services.PostService
import com.ivanbartolelli.kotlinrepos.features.repositories.domain.models.toPost
import kotlinx.coroutines.flow.map


class PostsRepoImpl(
    private val postService: PostService,
    private val database: PostsDatabase
) : PostsRepo {
    @OptIn(ExperimentalPagingApi::class)
    override fun getPosts() = Pager(
        config = PagingConfig(
            pageSize = PostsConstants.ITEMS_PER_PAGE,
            prefetchDistance = PostsConstants.ITEMS_PREFETCH_DISTANCE
        ),
        pagingSourceFactory = {
            database.postsDao().getPosts()
        },
        remoteMediator = PostRemoteMediator(postService, database)
    ).flow.map { it.map { data -> data.toPost() } }

    override suspend fun cleanPostsCache() {
        database.clearAllTables()
    }
}


