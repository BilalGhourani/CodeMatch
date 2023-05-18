package com.task.codematch.data.source.local.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName


@Entity(tableName = "User")
data class User constructor(
    @PrimaryKey
    var id: Long = 0,
    var name: String = "",
    var username: String = "",
    var email: String = "",
    var phone: String = "",
    var website: String = "",
    @Embedded var address: Address,
    @Embedded var company: Company,
    var isFavorite: Int = 0
)

data class Company(
    @SerializedName("name")
    val cname: String = "",
    val catchPhrase: String = "",
    val bs: String = ""
)

data class Address(
    val street: String = "",
    val suite: String = "",
    val city: String = "",
    val zipcode: String = "",
    @Embedded var geo: Geo
)

data class Geo(
    val lat: Double = 0.0,
    val lng: Double = 0.0
)

