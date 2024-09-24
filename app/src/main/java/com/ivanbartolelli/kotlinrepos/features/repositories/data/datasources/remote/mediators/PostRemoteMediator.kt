package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.PostsDatabase
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities.PostEntity
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities.PostPagingInfoEntity
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities.toEntity
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.services.PostService
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.utils.PostsConstants

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
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

            val currentPagingInfo = getCurrentPagingInfo(loadType, state, database)

            val postsResponse = service.getPosts(
                nextId = currentPagingInfo?.nextId,
                previousId = currentPagingInfo?.previousId,
                limit = PostsConstants.ITEMS_PER_PAGE
            ).also { response ->
                response.data.children.forEach { postDto ->
                    postDto.data.timestamp = System.currentTimeMillis()
                }
            }

            val endOfPaginationReached = postsResponse.data.children.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.postsPagingInfoDao().clearAll()
                    database.postsDao().clearAll()
                }

                val postsPagingInfo = postsResponse.data.children.map { postDto ->
                    PostPagingInfoEntity(
                        id = postDto.data.id,
                        previousId = postsResponse.data.previousId,
                        nextId = postsResponse.data.nextId,
                        timestamp = postDto.data.timestamp
                    )
                }

                database.postsPagingInfoDao().insertAll(postsPagingInfo)
                database.postsDao()
                    .insertAll(postsResponse.data.children.map { postDto -> postDto.data.toEntity() })
            }

            MediatorResult.Success(
                endOfPaginationReached = endOfPaginationReached
            )

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }


    private suspend fun getCurrentPagingInfo(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>,
        database: PostsDatabase
    ): PostPagingInfoEntity? {
        return when (loadType) {
            LoadType.REFRESH -> {
                database.postsPagingInfoDao().getNewest()
                    ?: kotlin.run {
                        state.anchorPosition?.let { position ->
                            state.closestItemToPosition(position)?.id?.let { id ->
                                database.postsPagingInfoDao().get(id)
                            }
                        }
                    }
            }

            LoadType.PREPEND -> {
                state.pages.firstOrNull { page ->
                    page.data.isNotEmpty()
                }?.data?.firstOrNull()?.id?.let { id ->
                    database.postsPagingInfoDao().get(id)
                }
            }

            LoadType.APPEND -> {
                state.pages.lastOrNull { page ->
                    page.data.isNotEmpty()
                }?.data?.lastOrNull()?.id?.let { id ->
                    database.postsPagingInfoDao().get(id)
                }
            }
        }
    }

}