package com.ivanbartolelli.assignment.features.posts.data.datasources.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ivanbartolelli.assignment.features.posts.data.datasources.local.database.DatabaseConstants.POST_TABLE_NAME
import com.ivanbartolelli.assignment.features.posts.data.datasources.remote.dtos.PostDto

@Entity(tableName = POST_TABLE_NAME)
data class PostEntity(

    @PrimaryKey
    val id: String,
    val permalink: String,
    val imageUrl: String,
    val thumbnail: String,
    val title: String,
    val body: String,
    val author: String,
    val commentsCount: Int,
    val ups: Int,
    val timestamp: Long,
    val nextPageId: String?
)

fun PostDto.toEntity(): PostEntity {
    return PostEntity(
        id,
        permalink,
        imageUrl,
        thumbnail,
        title,
        body,
        author,
        commentsCount,
        ups,
        timestamp,
        nextPageId
    )
}