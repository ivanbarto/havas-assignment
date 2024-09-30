package com.ivanbartolelli.assignment.features.posts.data.repos

import androidx.paging.PagingData
import com.ivanbartolelli.assignment.features.posts.domain.models.Post
import kotlinx.coroutines.flow.Flow

interface PostsRepo {
    fun getPosts(): Flow<PagingData<Post>>
}