package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities.PostPagingInfoEntity
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.utils.DatabaseConstants.POST_PAGING_INFO_TABLE_NAME

@Dao
interface PostPagingInfoDao {

    @Query("DELETE FROM $POST_PAGING_INFO_TABLE_NAME")
    suspend fun clearAll()

    @Query("SELECT * FROM $POST_PAGING_INFO_TABLE_NAME WHERE id = :id ORDER BY timestamp ASC")
    suspend fun get(id: String): PostPagingInfoEntity

    @Query("SELECT * FROM $POST_PAGING_INFO_TABLE_NAME ORDER BY timestamp DESC LIMIT 1")
    suspend fun getNewest(): PostPagingInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(postPagingInfoEntities: List<PostPagingInfoEntity>)

}