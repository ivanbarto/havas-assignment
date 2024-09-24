package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.utils.DatabaseConstants.POST_TABLE_NAME
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dtos.PostDto

@Entity(tableName = POST_TABLE_NAME)
data class PostEntity(

    @PrimaryKey
    val id: String,
    val permalink: String,
    val imageUrl: String,
    val title: String,
    val body: String,
    val author: String,
    val commentsCount: Int,
    val ups: Int,
    val timestamp: Long
)

fun PostDto.toEntity(): PostEntity {
    return PostEntity(id, permalink, imageUrl, title, body, author, commentsCount, ups, timestamp)
}