package com.ivanbartolelli.assignment.features.repositories.data.datasources.local.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ivanbartolelli.assignment.features.repositories.data.datasources.local.database.entities.PostEntity
import com.ivanbartolelli.assignment.features.repositories.data.datasources.local.database.utils.DatabaseConstants.POST_TABLE_NAME

@Dao
interface PostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<PostEntity>)

    @Query("DELETE FROM $POST_TABLE_NAME")
    suspend fun clearAll()

    @Query("SELECT * FROM $POST_TABLE_NAME ORDER BY timestamp ASC")
    fun getPosts(): PagingSource<Int, PostEntity>
}