package com.ivanbartolelli.kotlinrepos.features.repositories.domain.models

import android.net.Uri
import android.os.Parcelable
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dto.RepositoryDTO
import kotlinx.parcelize.Parcelize


@Parcelize
data class Repository(
    val id: Long?,
    val name: String?,
    val description: String?,
    val sshUrl: String?,
    val gitUrl: String?,
    val updatedAt: String?,
    val createdAt: String?,
    val watchersCount: Long?,
    val owner: Owner?
) : Parcelable {

    fun httpUrl(): String? = gitUrl?.replace("git://", "https://")

    fun urlAsUri(): Uri? = try {
        Uri.parse(httpUrl())
    } catch (e: Exception) {
        null
    }
}


fun RepositoryDTO.toRepository(): Repository =
    Repository(id, name, description, sshUrl, gitUrl, updatedAt, createdAt, watchersCount, owner?.toOwner())
