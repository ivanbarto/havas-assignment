package com.ivanbartolelli.kotlinrepos.features.repositories.domain.models

import android.net.Uri
import android.os.Parcelable
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities.PostEntity
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
) : Parcelable {

    fun httpUrl(): String = "https://reddit.com$permalink"

    fun urlAsUri(): Uri? = try {
        Uri.parse(httpUrl())
    } catch (e: Exception) {
        null
    }
}


fun PostEntity.toPost(): Post =
    Post(id, permalink, imageUrl, thumbnail, title, body, author, commentsCount, ups, timestamp)
