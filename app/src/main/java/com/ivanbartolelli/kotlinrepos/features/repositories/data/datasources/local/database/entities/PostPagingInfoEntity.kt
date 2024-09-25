package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.utils.DatabaseConstants.POST_PAGING_INFO_TABLE_NAME

@Entity(tableName = POST_PAGING_INFO_TABLE_NAME)
data class PostPagingInfoEntity(
    @PrimaryKey
    val id: String,
    val nextId: String?,
    val timestamp: Long
)