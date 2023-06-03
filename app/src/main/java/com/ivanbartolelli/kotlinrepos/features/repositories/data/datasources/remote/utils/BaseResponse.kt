package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.utils

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("incomplete_results")
    val areResultsIncomplete: Boolean,
    @SerializedName("items")
    val items: List<T>
)