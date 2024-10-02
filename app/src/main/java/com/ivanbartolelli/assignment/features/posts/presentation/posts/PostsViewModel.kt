package com.ivanbartolelli.assignment.features.posts.presentation.posts

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ivanbartolelli.assignment.core.presentation.BaseViewModel
import com.ivanbartolelli.assignment.features.posts.data.repos.PostsRepo
import com.ivanbartolelli.assignment.features.posts.domain.interactor.PostsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(paginatedPostsRepo: PostsInteractor) :
    BaseViewModel() {

    val posts = paginatedPostsRepo.getPosts().cachedIn(viewModelScope)
}