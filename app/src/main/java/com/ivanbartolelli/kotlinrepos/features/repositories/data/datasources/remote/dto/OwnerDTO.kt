package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dto

import com.google.gson.annotations.SerializedName

data class OwnerDTO(
    @SerializedName("login")
    val userName : String?,
    @SerializedName("avatar_url")
    val avatarUrl : String?,
    @SerializedName("url")
    val profileUrl : String?
)