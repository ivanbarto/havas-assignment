package com.ivanbartolelli.assignment.features.posts.data.datasources.remote.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ivanbartolelli.assignment.features.posts.data.datasources.local.database.PostsDatabase
import com.ivanbartolelli.assignment.features.posts.data.datasources.local.database.entities.PostEntity
import com.ivanbartolelli.assignment.features.posts.data.datasources.local.database.entities.toEntity
import com.ivanbartolelli.assignment.features.posts.data.datasources.remote.mediators.RemoteMediatorConstants.ITEMS_PER_PAGE
import com.ivanbartolelli.assignment.features.posts.data.datasources.remote.services.PostService

private object RemoteMediatorConstants {
    const val ITEMS_PER_PAGE = 7
}

@OptIn(ExperimentalPagingApi::class)
class PostsRemoteMediator(
    private val service: PostService,
    private val database: PostsDatabase
) :
    RemoteMediator<Int, PostEntity>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        return try {
            when (loadType) {
                LoadType.PREPEND -> MediatorResult.Success(endOfPaginationReached = true)
                else -> {
                    val currentPagingInfo = nextPageId(loadType, database)

                    val postsResponse = service.getPostsPage(
                        nextId = currentPagingInfo,
                        limit = ITEMS_PER_PAGE
                    ).also { response ->
                        response.data.children.forEach { postDto ->
                            postDto.data.timestamp = System.currentTimeMillis()
                            postDto.data.nextPageId = response.data.nextId
                        }
                    }

                    val endOfPaginationReached = postsResponse.data.children.isEmpty()

                    database.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            database.postsDao().clearAll()
                        }

                        database.postsDao()
                            .insertAll(postsResponse.data.children.map { postDto -> postDto.data.toEntity() })
                    }
                    MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }


    private suspend fun nextPageId(
        loadType: LoadType,
        database: PostsDatabase
    ): String? {
        return when (loadType) {
            LoadType.PREPEND, LoadType.REFRESH -> {
                null
            }

            LoadType.APPEND -> {
                database.postsDao().getLastInserted().nextPageId
            }
        }
    }
}