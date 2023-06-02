package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.daos

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities.RepositoryEntity
import kotlinx.coroutines.flow.Flow

interface RepositoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepository(repositoryEntity: RepositoryEntity)

    @Query("SELECT * FROM credentials")
    fun getRepositories(): Flow<List<RepositoryEntity>>

}