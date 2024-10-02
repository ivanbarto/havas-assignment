package com.ivanbartolelli.assignment.features.posts.data.datasources.remote.dtos

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("permalink")
    val permalink: String,
    @SerializedName("url")
    val imageUrl: String,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("selftext")
    val body: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("num_comments")
    val commentsCount: Int,
    @SerializedName("ups")
    val ups: Int,
    var timestamp: Long,
    var nextPageId: String?
)
