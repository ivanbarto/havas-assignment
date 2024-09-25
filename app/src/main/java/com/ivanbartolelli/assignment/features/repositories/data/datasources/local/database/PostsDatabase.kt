package com.ivanbartolelli.assignment.features.repositories.data.datasources.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ivanbartolelli.assignment.features.repositories.data.datasources.local.database.daos.PostsDao
import com.ivanbartolelli.assignment.features.repositories.data.datasources.local.database.daos.PostPagingInfoDao
import com.ivanbartolelli.assignment.features.repositories.data.datasources.local.database.entities.PostEntity
import com.ivanbartolelli.assignment.features.repositories.data.datasources.local.database.entities.PostPagingInfoEntity


@Database(
    entities = [PostEntity::class, PostPagingInfoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PostsDatabase : RoomDatabase() {
    abstract fun postsDao(): PostsDao
    abstract fun postsPagingInfoDao(): PostPagingInfoDao
}