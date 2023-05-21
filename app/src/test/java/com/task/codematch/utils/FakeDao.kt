package com.raghav.mynotes.utils

import com.task.codematch.data.source.local.dao.UserDao
import com.task.codematch.data.source.local.entity.Address
import com.task.codematch.data.source.local.entity.Company
import com.task.codematch.data.source.local.entity.Geo
import com.task.codematch.data.source.local.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDao : UserDao {

    private val usersList = mutableListOf(
        User(
            1,
            "John", "John_username",
            "John@xxx.com",
            "8084098134",
            "John.com",
            Address("street", "suite", "city", "zipcode", Geo(80.0809, 90.979)),
            Company("name", "catch_phrase", "bs"),
            isFavorite = 1
        ), User(
            2,
            "Jane", "Jane_username",
            "Jane@xxx.com",
            "837419712",
            "Jane.com",
            Address("street", "suite", "city", "zipcode", Geo(80.0809, 90.979)),
            Company("name", "catch_phrase", "bs"),
            isFavorite = 0
        )
    )

    override suspend fun getUsers(): MutableList<User> {
        return usersList
    }

    override suspend fun getFavoriteUsers(): MutableList<User> {
        var favoriteList = mutableListOf<User>()
        for (user in usersList) {
            if (user.isFavorite == 1) {
                favoriteList.add(user)
            }
        }
        return favoriteList
    }

    override fun getFlowUserById(id: Long): Flow<User> {
        for (user in usersList) {
            if (user.id == id) {
                return flow { emit(user) }
            }
        }
        return flow { emit(getEmptyUser()) }
    }

    override fun getUserById(id: Long): User {
        for (user in usersList) {
            if (user.id == id) {
                return user
            }
        }
        return getEmptyUser()
    }

    override suspend fun insertUser(user: User) {
        usersList.add(user)
    }

    fun getEmptyUser(): User {
        return User(
            -1,
            "", "",
            "",
            "",
            "",
            Address("", "", "", "", Geo(0.0, 0.0)),
            Company("", "", "")
        )
    }
}