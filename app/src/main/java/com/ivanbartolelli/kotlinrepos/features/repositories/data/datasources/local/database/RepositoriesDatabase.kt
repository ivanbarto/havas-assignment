package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.converters.RepositoriesTypeConverter
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.daos.RepositoriesDao
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.daos.RepositoriesPagingInfoDao
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities.RepositoryEntity
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities.RepositoryPagingInfoEntity


@Database(
    entities = [RepositoryEntity::class, RepositoryPagingInfoEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RepositoriesTypeConverter::class)
abstract class RepositoriesDatabase : RoomDatabase() {
    abstract fun repositoriesDao(): RepositoriesDao
    abstract fun repositoriesPagingInfoDao(): RepositoriesPagingInfoDao
}