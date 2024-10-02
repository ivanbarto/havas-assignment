package com.ivanbartolelli.assignment.features.posts.data.repos

import androidx.paging.PagingSource
import com.ivanbartolelli.assignment.features.posts.data.datasources.local.database.entities.PostEntity

interface PostsRepo {
    fun getPosts():PagingSource<Int, PostEntity>
}