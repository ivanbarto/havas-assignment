package com.ivanbartolelli.kotlinrepos.features.repositories.presentation.repositories

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ivanbartolelli.kotlinrepos.core.presentation.BaseViewModel
import com.ivanbartolelli.kotlinrepos.features.repositories.data.repos.PostsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(private val paginatedPostsRepo: PostsRepo) :
    BaseViewModel() {

    val repositories = paginatedPostsRepo.getPosts().cachedIn(viewModelScope)

    suspend fun clearRepositoriesCache() {
        paginatedPostsRepo.cleanPostsCache()
    }

}