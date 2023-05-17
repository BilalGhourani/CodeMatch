package com.task.codematch.data.source.repository

import androidx.annotation.MainThread
import com.task.codematch.data.source.local.dao.UserDao
import com.task.codematch.data.source.local.entity.User
import com.task.codematch.data.source.remote.Resource
import com.task.codematch.data.source.remote.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import retrofit2.Response
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val usersDao: UserDao,
    private val userService: UserService
) : UserRepository {


    /**
     * Fetched the users from network and stored it in database. At the end, data from persistence
     * storage is fetched and emitted.
     */
    override suspend fun getAllUsers(): Flow<Resource<List<User>>> {
        return object : NetworkBoundRepository<List<User>, List<User>>() {

            override suspend fun saveRemoteData(response: List<User>) = usersDao.insertUsers(response)

            override suspend fun fetchFromLocal(): List<User> = usersDao.getUsers()

            override suspend fun fetchFromRemote(): List<User> = userService.getAllUsers()
        }.asFlow()
    }

    override suspend fun saveUser(user: User) = usersDao.insertUser(user)

    /**
     * Retrieves an user with specified [userId].
     * @param userId Unique id of a [User].
     * @return [User] data fetched from the database.
     */
    @MainThread
    override suspend fun getUserById(userId: Long): Flow<User> =
        usersDao.getUserById(userId).distinctUntilChanged()

}