package com.ivanbartolelli.kotlinrepos.features.repositories.data.repos

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.RepositoriesDatabase
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dto.RepositoriesConstants
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.paginatedSources.RepositoriesRemoteMediator
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.services.RepositoriesService
import com.ivanbartolelli.kotlinrepos.features.repositories.domain.models.toRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class RepositoriesRepoImpl (private val repositoriesService: RepositoriesService, private val database: RepositoriesDatabase) : RepositoriesRepo  {
    @OptIn(ExperimentalPagingApi::class)
    override fun getRepositories() = Pager(
        config = PagingConfig(
            pageSize = RepositoriesConstants.ITEMS_PER_PAGE,
            prefetchDistance = RepositoriesConstants.ITEMS_PREFETCH_DISTANCE
        ),
        pagingSourceFactory = {
            database.repositoriesDao().getRepositories()
        },
        remoteMediator = RepositoriesRemoteMediator(repositoriesService, database)
    ).flow.map { it.map {data -> data.toRepository() } }
}


