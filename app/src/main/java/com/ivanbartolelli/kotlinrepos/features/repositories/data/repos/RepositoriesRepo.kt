package com.ivanbartolelli.kotlinrepos.features.repositories.data.repos

import androidx.paging.PagingData
import com.ivanbartolelli.kotlinrepos.features.repositories.domain.models.Repository
import kotlinx.coroutines.flow.Flow

interface RepositoriesRepo {
    fun getRepositories(): Flow<PagingData<Repository>>

    suspend fun clearRepositoriesCache()
}