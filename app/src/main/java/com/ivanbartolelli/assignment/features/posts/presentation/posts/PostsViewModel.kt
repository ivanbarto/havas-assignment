package com.ivanbartolelli.assignment.features.posts.presentation.posts

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ivanbartolelli.assignment.core.presentation.BaseViewModel
import com.ivanbartolelli.assignment.features.posts.domain.interactor.PostsInteractor
import com.ivanbartolelli.assignment.features.posts.domain.models.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(private val postsInteractor: PostsInteractor) :
    BaseViewModel() {

    val posts = postsInteractor.getPosts()
        .cachedIn(viewModelScope)
}