package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.paginatedSources

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.RepositoriesDatabase
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities.RepositoryEntity
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities.toEntity
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dto.RepositoriesConstants
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dto.RepositoryDTO
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.services.RepositoriesService
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.utils.RepositoriesQueries
import okio.IOException
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class) class RepositoriesRemoteMediator(
    private val service: RepositoriesService,
    private val database: RepositoriesDatabase
) :
    RemoteMediator<Int, RepositoryEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, RepositoryEntity>): MediatorResult {
        return try {

            val response = service.getRepositoriesByLanguage(
                language = RepositoriesQueries.LANGUAGE_KOTLIN,
                page = state.anchorPosition
                    ?: 0,
                itemsPerPage = RepositoriesConstants.ITEMS_PER_PAGE
            )

            database.withTransaction {
                database.repositoriesDao().insertAll(response.items.map { it.toEntity() })
            }

            MediatorResult.Success(
                endOfPaginationReached = response.items.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

}