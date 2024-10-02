package com.ivanbartolelli.assignment.features.posts.domain.interactor

import androidx.paging.PagingData
import com.ivanbartolelli.assignment.features.posts.domain.models.Post
import kotlinx.coroutines.flow.Flow

interface PostsInteractor {
    fun getPosts(): Flow<PagingData<Post>>
}