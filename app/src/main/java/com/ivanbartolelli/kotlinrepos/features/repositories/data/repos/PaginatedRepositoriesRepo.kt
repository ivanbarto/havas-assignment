package com.ivanbartolelli.kotlinrepos.features.repositories.data.repos

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.services.RepositoriesService
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dto.RepositoriesConstants
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.paginatedSources.RepositoriesSource
import javax.inject.Inject


class PaginatedRepositoriesRepo @Inject constructor(private val repositoriesService: RepositoriesService)  {
    fun getRepositories() = Pager(
        config = PagingConfig(
            pageSize = RepositoriesConstants.ITEMS_PER_PAGE,
            prefetchDistance = RepositoriesConstants.ITEMS_PREFETCH_DISTANCE
        ),
        pagingSourceFactory = { RepositoriesSource(repositoriesService) }
    ).flow
}


