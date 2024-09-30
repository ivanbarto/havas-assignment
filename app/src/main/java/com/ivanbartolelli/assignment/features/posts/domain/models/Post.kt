package com.ivanbartolelli.assignment.features.posts.domain.models

import android.net.Uri
import android.os.Parcelable
import com.ivanbartolelli.assignment.features.posts.data.datasources.local.database.entities.PostEntity
import kotlinx.parcelize.Parcelize


@Parcelize
data class Post(
    val id: String?,
    val permalink: String,
    val imageUrl: String,
    val thumbnail: String,
    val title: String,
    val body: String,
    val author: String,
    val commentsCount: Int,
    val ups: Int,
    val timestamp: Long
) : Parcelable

fun PostEntity.toPost(): Post =
    Post(id, permalink, imageUrl, thumbnail, title, body, author, commentsCount, ups, timestamp)
