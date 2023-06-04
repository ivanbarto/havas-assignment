package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.RepositoriesDatabase
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities.RepositoryEntity
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities.RepositoryPagingInfoEntity
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities.toEntity
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.services.RepositoriesService
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.utils.RemoteMediatorConstants.FIRST_PAGE
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.utils.RemoteMediatorConstants.PAGING_DECREMENT
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.utils.RemoteMediatorConstants.PAGING_INCREMENT
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.utils.RepositoriesConstants
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.utils.RepositoriesQueries

@OptIn(ExperimentalPagingApi::class)
class RepositoriesRemoteMediator(
    private val service: RepositoriesService,
    private val database: RepositoriesDatabase
) :
    RemoteMediator<Int, RepositoryEntity>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, RepositoryEntity>): MediatorResult {
        return try {

            val currentPagingInfo = getCurrentPagingInfo(loadType, state, database)

            val currentPage: Int? = when (loadType) {
                LoadType.REFRESH -> currentPagingInfo?.nextPage
                    ?: FIRST_PAGE
                LoadType.PREPEND -> currentPagingInfo?.previousPage
                LoadType.APPEND -> currentPagingInfo?.nextPage
            }

            currentPage?.let {

                val repositoriesResponse = service.getRepositoriesByLanguage(
                    language = RepositoriesQueries.LANGUAGE_KOTLIN,
                    page = it,
                    itemsPerPage = RepositoriesConstants.ITEMS_PER_PAGE
                ).also { response ->
                    response.items.forEach { repositoryDto -> repositoryDto.timestamp = System.currentTimeMillis() }
                }

                val endOfPaginationReached = repositoriesResponse.items.isEmpty()

                val previousPage = if (it == FIRST_PAGE) null else it.minus(PAGING_DECREMENT)
                val nextPage = if (endOfPaginationReached) null else it.plus(PAGING_INCREMENT)

                database.withTransaction {
                    if (loadType == LoadType.REFRESH && currentPage == FIRST_PAGE) {
                        database.repositoriesPagingInfoDao().clearAll()
                        database.repositoriesDao().clearAll()
                    }

                    val repositoriesPagingInfo = repositoriesResponse.items.map { repositoryDto ->
                        RepositoryPagingInfoEntity(
                            repositoryDto.id,
                            previousPage,
                            nextPage,
                            repositoryDto.timestamp
                        )
                    }

                    database.repositoriesPagingInfoDao().insertAll(repositoriesPagingInfo)
                    database.repositoriesDao().insertAll(repositoriesResponse.items.map { repositoryDto -> repositoryDto.toEntity() })
                }

                MediatorResult.Success(
                    endOfPaginationReached = endOfPaginationReached
                )

            }
                ?: MediatorResult.Success(endOfPaginationReached = currentPagingInfo != null)

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }


    private suspend fun getCurrentPagingInfo(
        loadType: LoadType,
        state: PagingState<Int, RepositoryEntity>,
        database: RepositoriesDatabase
    ): RepositoryPagingInfoEntity? {
        return when (loadType) {
            LoadType.REFRESH -> {
                database.repositoriesPagingInfoDao().getNewest()
                    ?: kotlin.run {
                        state.anchorPosition?.let { position ->
                            state.closestItemToPosition(position)?.id?.let { id ->
                                database.repositoriesPagingInfoDao().get(id)
                            }
                        }
                    }
            }
            LoadType.PREPEND -> {
                state.pages.firstOrNull { page ->
                    page.data.isNotEmpty()
                }?.data?.firstOrNull()?.id?.let { id ->
                    database.repositoriesPagingInfoDao().get(id)
                }
            }
            LoadType.APPEND -> {
                state.pages.lastOrNull { page ->
                    page.data.isNotEmpty()
                }?.data?.lastOrNull()?.id?.let { id ->
                    database.repositoriesPagingInfoDao().get(id)
                }
            }
        }
    }

}