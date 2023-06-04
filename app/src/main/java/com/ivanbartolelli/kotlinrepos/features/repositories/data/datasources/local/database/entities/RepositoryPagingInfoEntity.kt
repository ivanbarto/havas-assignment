package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.utils.DatabaseConstants.REPOSITORY_PAGING_INFO_TABLE_NAME

@Entity(tableName = REPOSITORY_PAGING_INFO_TABLE_NAME)
data class RepositoryPagingInfoEntity(
    @PrimaryKey(autoGenerate = false)
    val repositoryId: Long?,
    val previousPage: Int?,
    val nextPage: Int?,
    val timestamp: Long,
)