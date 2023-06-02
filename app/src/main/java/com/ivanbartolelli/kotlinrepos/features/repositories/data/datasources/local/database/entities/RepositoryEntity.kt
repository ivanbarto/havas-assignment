package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repositories")
data class RepositoryEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    val name: String? = null,
    val description: String? = null,
    val sshUrl: String? = null,
    val gitUrl: String? = null,
    val updatedAt: String? = null,
    val createdAt: String? = null,
    val watchersCount: Long? = null,
    val owner: OwnerEntity? = null
)


data class OwnerEntity(
    val userName: String?,
    val avatarUrl: String?,
    val profileUrl: String?
)