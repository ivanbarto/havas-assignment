package com.ivanbartolelli.assignment.features.posts.data.datasources.remote.services

import com.ivanbartolelli.assignment.features.posts.data.datasources.remote.dtos.BaseResponseDto
import com.ivanbartolelli.assignment.features.posts.data.datasources.remote.dtos.PostDto
import com.ivanbartolelli.assignment.features.posts.data.datasources.remote.utils.PostUrls
import retrofit2.http.GET
import retrofit2.http.Query

interface PostService {

    @GET(PostUrls.POSTS)
    suspend fun getPostsPage(
        @Query("after") nextId: String?,
        @Query("limit") limit: Int
    ): BaseResponseDto<PostDto>
}