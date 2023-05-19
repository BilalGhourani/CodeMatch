package com.task.codematch.data.source.repository

import androidx.annotation.MainThread
import com.task.codematch.data.model.UserModel
import com.task.codematch.data.source.local.dao.UserDao
import com.task.codematch.data.source.local.entity.User
import com.task.codematch.data.source.remote.Resource
import com.task.codematch.data.source.remote.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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
    override suspend fun getAllUsers(): Flow<Resource<MutableList<User>>> {
        return object : NetworkBoundRepository<MutableList<User>, MutableList<User>>() {

            override suspend fun saveRemoteData(response: MutableList<UserModel>) {
                for (userMpdel in response) {
                    var user = usersDao.getUserById(userMpdel.id);
                    if (user != null) {
                        user.name = userMpdel.name
                        user.username = userMpdel.username
                        user.email = userMpdel.email
                        user.phone = userMpdel.phone
                        user.website = userMpdel.website
                        user.address = userMpdel.address
                        user.company = userMpdel.company
                    } else {
                        user = User(
                            userMpdel.id,
                            userMpdel.name,
                            userMpdel.username,
                            userMpdel.email,
                            userMpdel.phone,
                            userMpdel.website,
                            userMpdel.address,
                            userMpdel.company
                        )
                    }
                    usersDao.insertUser(user)
                }
            }

            override suspend fun fetchFromLocal(): MutableList<User> = usersDao.getUsers()

            override suspend fun fetchFromRemote(): MutableList<UserModel> = userService.getAllUsers()
        }.asFlow()
    }

    override suspend fun getAllLocalUsers(): Flow<Resource<MutableList<User>>> {
        var asFlow = flow<Resource<MutableList<User>>> {
            var favoriteUsers = usersDao.getUsers()
            emit(Resource.Success(favoriteUsers))
        }
        return asFlow
    }

    override suspend fun getAllFavoriteUsers(): Flow<Resource<MutableList<User>>> {
        var asFlow = flow<Resource<MutableList<User>>> {
            var favoriteUsers = usersDao.getFavoriteUsers()
            emit(Resource.Success(favoriteUsers))
        }
        return asFlow
    }

    override suspend fun saveUser(user: User) = usersDao.insertUser(user)

    /**
     * Retrieves an user with specified [userId].
     * @param userId Unique id of a [User].
     * @return [User] data fetched from the database.
     */
    @MainThread
    override suspend fun getUserById(userId: Long): Flow<User> =
        usersDao.getFlowUserById(userId).distinctUntilChanged()

}