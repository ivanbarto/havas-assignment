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

            val repositoriesResponse = service.getPosts(
                nextId = currentPagingInfo?.nextId,
                previousId = currentPagingInfo?.previousId,
                limit = PostsConstants.ITEMS_PER_PAGE
            ).also { response ->
                response.data.items.forEach { repositoryDto ->
                    repositoryDto.timestamp = System.currentTimeMillis()
                }
            }

            val endOfPaginationReached = repositoriesResponse.data.items.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.postsPagingInfoDao().clearAll()
                    database.postsDao().clearAll()
                }

                val repositoriesPagingInfo = repositoriesResponse.data.items.map { repositoryDto ->
                    PostPagingInfoEntity(
                        id = repositoryDto.id,
                        previousId = repositoriesResponse.data.previousId,
                        nextId = repositoriesResponse.data.nextId,
                        timestamp = repositoryDto.timestamp
                    )
                }

                database.postsPagingInfoDao().insertAll(repositoriesPagingInfo)
                database.postsDao()
                    .insertAll(repositoriesResponse.data.items.map { repositoryDto -> repositoryDto.toEntity() })
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