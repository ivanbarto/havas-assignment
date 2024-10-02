package com.ivanbartolelli.assignment.features.posts.data.datasources.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ivanbartolelli.assignment.features.posts.data.datasources.local.database.daos.PostsDao
import com.ivanbartolelli.assignment.features.posts.data.datasources.local.database.entities.PostEntity


@Database(
    entities = [PostEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PostsDatabase : RoomDatabase() {
    abstract fun postsDao(): PostsDao
}