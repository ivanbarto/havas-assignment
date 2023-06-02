package com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.converters

import com.google.gson.Gson
import com.ivanbartolelli.kotlinrepos.features.repositories.data.datasources.local.database.entities.OwnerEntity

class RepositoriesTypeConverter {

    var gson = Gson()

    @androidx.room.TypeConverter
    fun fromOwner(owner: OwnerEntity): String {
        return gson.toJson(owner)
    }

    @androidx.room.TypeConverter
    fun toOwner(json: String): OwnerEntity {
        return gson.fromJson(json, OwnerEntity::class.java)
    }
}