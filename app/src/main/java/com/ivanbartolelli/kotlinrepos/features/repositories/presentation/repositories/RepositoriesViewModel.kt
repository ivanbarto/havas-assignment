package com.ivanbartolelli.kotlinrepos.features.repositories.presentation.repositories

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ivanbartolelli.kotlinrepos.core.base_ui.BaseViewModel
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.RepositoriesService
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dto.RepositoriesConstants
import com.ivanbartolelli.kotlinrepos.features.repositories.data.repos.PaginatedRepositoriesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(private val service: RepositoriesService) : BaseViewModel() {

    val repositories = Pager(
        config = PagingConfig(
            pageSize = RepositoriesConstants.ITEMS_PER_PAGE,
            prefetchDistance = RepositoriesConstants.ITEMS_PREFETCH_DISTANCE
        ),
        pagingSourceFactory = { PaginatedRepositoriesRepo(service) }
    ).flow.cachedIn(viewModelScope)

}