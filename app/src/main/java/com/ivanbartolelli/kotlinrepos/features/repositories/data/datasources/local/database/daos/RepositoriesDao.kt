package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities.RepositoryEntity
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dto.RepositoriesConstants.DATABASE_REPOSITORY_TABLE_NAME

@Dao
interface RepositoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<RepositoryEntity>)

    @Query("DELETE FROM $DATABASE_REPOSITORY_TABLE_NAME")
    suspend fun clearAll()

    @Query("SELECT * FROM $DATABASE_REPOSITORY_TABLE_NAME")
    fun getRepositories(): PagingSource<Int, RepositoryEntity>


}