package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.remote.dto

object RepositoriesConstants {
    const val ITEMS_PER_PAGE = 20
    const val ITEMS_PREFETCH_DISTANCE = 2
    const val FIRST_PAGE_INDEX = 1
    const val REFRESH_KEY_INCREMENT = 1

    private const val MAX_ITEMS_AVAILABLE = 1000

    const val DATABASE_NAME = "repositoriesDatabase"
    const val DATABASE_REPOSITORY_TABLE_NAME = "repositories"

    fun maxPagesAvailable() : Long {
        return (MAX_ITEMS_AVAILABLE / ITEMS_PER_PAGE).toLong()
    }
}