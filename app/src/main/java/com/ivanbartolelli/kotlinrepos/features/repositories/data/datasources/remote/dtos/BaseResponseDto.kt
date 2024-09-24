package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dtos

import com.google.gson.annotations.SerializedName

data class BaseResponseDto<T>(
    @SerializedName("kind")
    val kind: Int,
    @SerializedName("data")
    val data: BaseDataDto<T>
)

data class BaseDataDto<T>(
    @SerializedName("after")
    val nextId: String?,
    @SerializedName("before")
    val previousId: String?,
    @SerializedName("children")
    val items: List<T>,
)