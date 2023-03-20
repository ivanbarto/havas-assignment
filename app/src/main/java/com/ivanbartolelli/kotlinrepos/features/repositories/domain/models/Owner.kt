package com.ivanbartolelli.kotlinrepos.features.repositories.domain.models

import android.os.Parcelable
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dto.OwnerDTO
import kotlinx.parcelize.Parcelize

@Parcelize
data class Owner(
    val userName: String?,
    val avatarUrl: String?,
    val profileUrl: String?
) : Parcelable

fun OwnerDTO.toOwner(): Owner = Owner(userName, avatarUrl, profileUrl)