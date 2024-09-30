package com.ivanbartolelli.assignment.features.posts.data.datasources.remote.dtos

import com.google.gson.annotations.SerializedName

data class BaseResponseDto<T>(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("data")
    val data: BaseDataDto<T>
)

data class BaseDataDto<T>(
    @SerializedName("after")
    val nextId: String?,
    @SerializedName("children")
    val children: List<BaseChildrenDto<T>>,
)

data class BaseChildrenDto<T>(
    @SerializedName("data")
    val data: T
)