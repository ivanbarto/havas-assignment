package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dto.OwnerDTO
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dto.RepositoriesConstants.DATABASE_REPOSITORY_TABLE_NAME
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dto.RepositoryDTO

@Entity(tableName = DATABASE_REPOSITORY_TABLE_NAME)
data class RepositoryEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Long?,
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

fun OwnerDTO.toEntity(): OwnerEntity {
    return OwnerEntity(userName, avatarUrl, profileUrl)
}

fun RepositoryDTO.toEntity(): RepositoryEntity {
    return RepositoryEntity(id, name, description, sshUrl, gitUrl, updatedAt, createdAt, watchersCount, owner?.toEntity())
}