package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.services

import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.utils.BaseResponse
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dto.RepositoryDTO
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.utils.RepositoriesUrls
import retrofit2.http.GET
import retrofit2.http.Query

interface RepositoriesService {

    @GET(RepositoriesUrls.SEARCH_REPOSITORIES)
    suspend fun getRepositoriesByLanguage(
        @Query("q") language: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): BaseResponse<RepositoryDTO>

}