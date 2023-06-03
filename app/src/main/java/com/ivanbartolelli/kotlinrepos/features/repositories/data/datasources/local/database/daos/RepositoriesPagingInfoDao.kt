package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities.RepositoryPagingInfoEntity
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.utils.REPOSITORY_PAGING_INFO_TABLE_NAME

@Dao
interface RepositoriesPagingInfoDao {

    @Query("DELETE FROM $REPOSITORY_PAGING_INFO_TABLE_NAME")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repositoryPagingInfoEntity: RepositoryPagingInfoEntity)

    @Query("SELECT * FROM $REPOSITORY_PAGING_INFO_TABLE_NAME WHERE id = :id ORDER BY timestamp ASC")
    suspend fun get(id: Long): RepositoryPagingInfoEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repositoryPagingInfoEntities: List<RepositoryPagingInfoEntity>)

}