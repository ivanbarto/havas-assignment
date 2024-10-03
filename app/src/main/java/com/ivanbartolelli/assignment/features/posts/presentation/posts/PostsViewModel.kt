package com.ivanbartolelli.assignment.features.posts.presentation.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ivanbartolelli.assignment.core.presentation.ScreenState
import com.ivanbartolelli.assignment.core.presentation.toErrorType
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
    ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState<PagingData<Post>>>(ScreenState.Loading)
    val screenState: StateFlow<ScreenState<PagingData<Post>>> = _screenState

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        viewModelScope.launch {
            _screenState.value = ScreenState.Loading
            try {
                postsInteractor.getPosts()
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _screenState.value = ScreenState.Success(pagingData)
                    }
            } catch (e: Exception) {
                _screenState.value = ScreenState.Error(e.toErrorType())
            }
        }
    }
}