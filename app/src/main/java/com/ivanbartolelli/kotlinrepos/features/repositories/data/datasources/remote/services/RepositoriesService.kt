package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.services

import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dtos.BaseResponseDto
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dtos.RepositoryDto
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.utils.RepositoriesUrls
import retrofit2.http.GET
import retrofit2.http.Query

interface RepositoriesService {

    @GET(RepositoriesUrls.SEARCH_REPOSITORIES)
    suspend fun getRepositoriesByLanguage(
        @Query("q") language: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): BaseResponseDto<RepositoryDto>

}