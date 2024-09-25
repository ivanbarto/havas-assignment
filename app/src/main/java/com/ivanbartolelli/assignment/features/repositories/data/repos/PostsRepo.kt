package com.ivanbartolelli.assignment.features.repositories.data.repos

import androidx.paging.PagingData
import com.ivanbartolelli.assignment.features.repositories.domain.models.Post
import kotlinx.coroutines.flow.Flow

interface PostsRepo {
    fun getPosts(): Flow<PagingData<Post>>

    suspend fun cleanPostsCache()
}