package com.ivanbartolelli.assignment.features.repositories.presentation.posts

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ivanbartolelli.assignment.core.presentation.BaseViewModel
import com.ivanbartolelli.assignment.features.repositories.data.repos.PostsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(paginatedPostsRepo: PostsRepo) :
    BaseViewModel() {

    val repositories = paginatedPostsRepo.getPosts().cachedIn(viewModelScope)
}