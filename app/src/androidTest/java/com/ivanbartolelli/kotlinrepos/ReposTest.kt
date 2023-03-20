package com.ivanbartolelli.kotlinrepos

import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.RepositoriesService
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dto.RepositoriesConstants
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.utils.RepositoriesQueries
import com.ivanbartolelli.kotlinrepos.features.repositories.data.repos.PaginatedRepositoriesRepo
import com.ivanbartolelli.kotlinrepos.features.repositories.domain.models.toRepository
import com.ivanbartolelli.kotlinrepos.features.repositories.presentation.repositories.adapters.RepositoriesAdapter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ReposTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repositoriesService: RepositoriesService

    @Inject
    lateinit var repositoriesAdapter: RepositoriesAdapter

    @Inject
    lateinit var paginatedRepositoriesRepo: PaginatedRepositoriesRepo


    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun repositories_service_is_working() = runBlocking {
        val itemsQuantity = 20

        val repositories = repositoriesService.getRepositoriesByLanguage(
            language = RepositoriesQueries.LANGUAGE_KOTLIN,
            page = 1,
            itemsPerPage = itemsQuantity
        ).items.map { it.toRepository() }

        assert(repositories.size == itemsQuantity)
    }

    @Test
    fun paginated_repositories_source_load_is_working() = runBlocking {
        val itemsQuantity = 20

        val repositoriesFromFirstPage = repositoriesService.getRepositoriesByLanguage(
            language = RepositoriesQueries.LANGUAGE_KOTLIN,
            page = 1,
            itemsPerPage = itemsQuantity
        ).items.map { it.toRepository() }

        val expectedResultFromFirstPage = PagingSource.LoadResult.Page(
            data = repositoriesFromFirstPage,
            prevKey = null,
            nextKey = 2
        )

        val repositoriesFromSecondPage = repositoriesService.getRepositoriesByLanguage(
            language = RepositoriesQueries.LANGUAGE_KOTLIN,
            page = 2,
            itemsPerPage = itemsQuantity
        ).items.map { it.toRepository() }

        val expectedResultFromSecondPage = PagingSource.LoadResult.Page(
            data = repositoriesFromSecondPage,
            prevKey = 1,
            nextKey = 3
        )


        val actualResultFromFirstPage = paginatedRepositoriesRepo.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = itemsQuantity,
                placeholdersEnabled = false
            )
        )

        assert(expectedResultFromFirstPage == actualResultFromFirstPage)
        assert(expectedResultFromSecondPage != actualResultFromFirstPage)
    }

}