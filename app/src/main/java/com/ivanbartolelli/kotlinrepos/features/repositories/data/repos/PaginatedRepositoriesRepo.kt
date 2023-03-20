package com.ivanbartolelli.kotlinrepos.features.repositories.data.repos

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.utils.RepositoriesQueries
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.RepositoriesService
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dto.RepositoriesConstants
import com.ivanbartolelli.kotlinrepos.features.repositories.domain.models.Repository
import com.ivanbartolelli.kotlinrepos.features.repositories.domain.models.toRepository
import javax.inject.Inject


class PaginatedRepositoriesRepo @Inject constructor(private val repositoriesService: RepositoriesService) : PagingSource<Int, Repository>() {

    override fun getRefreshKey(state: PagingState<Int, Repository>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(RepositoriesConstants.REFRESH_KEY_INCREMENT)
                ?: anchorPage?.nextKey?.minus(RepositoriesConstants.REFRESH_KEY_INCREMENT)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {
        val pageNumber = params.key
            ?: RepositoriesConstants.FIRST_PAGE_INDEX

        return try {

            val response = repositoriesService.getRepositoriesByLanguage(
                language = RepositoriesQueries.LANGUAGE_KOTLIN,
                page = pageNumber,
                itemsPerPage = RepositoriesConstants.ITEMS_PER_PAGE
            )

            val repositories = response.items.map { it.toRepository() }

            var nextPageNumber: Int? = null
            if (RepositoriesConstants.maxPagesAvailable() > pageNumber) {
                nextPageNumber = pageNumber.inc()
            }

            var previousPageNumber: Int? = null
            if (pageNumber > RepositoriesConstants.FIRST_PAGE_INDEX) {
                previousPageNumber = pageNumber.dec()
            }

            LoadResult.Page(
                data = repositories,
                prevKey = previousPageNumber,
                nextKey = nextPageNumber
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}