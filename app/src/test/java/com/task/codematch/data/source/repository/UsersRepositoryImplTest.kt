package com.task.codematch.data.source.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raghav.mynotes.utils.FakeDao
import com.task.codematch.data.model.UserDTO
import com.task.codematch.data.source.local.entity.Address
import com.task.codematch.data.source.local.entity.Company
import com.task.codematch.data.source.local.entity.Geo
import com.task.codematch.data.source.local.entity.User
import com.task.codematch.data.source.remote.Resource
import com.task.codematch.data.source.remote.UserService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
internal class UsersRepositoryImplTest : UserRepository {

    private lateinit var tasksRepository: UserRepository
    private val fakeDao = FakeDao()
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

    @Before
    fun setupRepository() {
        tasksRepository = UsersRepositoryImpl(fakeDao, object : UserService {
            override suspend fun getAllUsers(): MutableList<UserDTO> {
                return mutableListOf(
                    UserDTO(
                        1,
                        "John", "John_username",
                        "John@xxx.com",
                        "8084098134",
                        "John.com",
                        Address("street", "suite", "city", "zipcode", Geo(80.0809, 90.979)),
                        Company("name", "catch_phrase", "bs")
                    ), UserDTO(
                        2,
                        "Jane", "Jane_username",
                        "Jane@xxx.com",
                        "837419712",
                        "Jane.com",
                        Address("street", "suite", "city", "zipcode", Geo(80.0809, 90.979)),
                        Company("name", "catch_phrase", "bs")
                    )
                )
            }
        })
    }

    override suspend fun getAllUsers(): Flow<Resource<MutableList<User>>> {
        return flow { emit(Resource.Success(usersList)) }
    }

    override suspend fun getAllFavoriteUsers(): Flow<Resource<MutableList<User>>> {
        var favoriteList = mutableListOf<User>()
        for (user in usersList) {
            if (user.isFavorite == 1) {
                favoriteList.add(user)
            }
        }
        return flow { emit(Resource.Success(favoriteList)) }
    }

    override suspend fun saveUser(user: User) {
        usersList.add(user)
    }

    override suspend fun getUserById(userId: Long): Flow<User> {
        for (user in usersList) {
            if (user.id == userId) {
                return flow { emit(user) }
            }
        }
        return flow { emit(getEmptyUser()) }
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