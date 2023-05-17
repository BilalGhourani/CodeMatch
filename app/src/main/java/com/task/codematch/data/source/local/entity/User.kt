package com.task.codematch.data.source.local.entity

import androidx.room.*


@Entity(tableName = "User")
data class User(
    @PrimaryKey
    var id: Long = 0,
    var name: String = "",
    var username: String = "",
    var email: String = "",
    var phone: String = "",
    var website: String = "",
    @Embedded var address: Address,
    @Embedded var company: Company,
)

data class Company(
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

/*
data class AddressWithGeo(
    @Embedded val address: Address,
    @Embedded val geo: Geo
)

data class UserWithAddressGeoAndCompany(
    @Embedded val user: User,
    @Relation(
        parentColumn = "addressId",
        entityColumn = "id"
    )
    val addressWithGeo: AddressWithGeo,
    @Relation(
        parentColumn = "companyId",
        entityColumn = "id"
    )
    val company: Company
)*/
