package com.task.codematch.data.source.repository

import android.util.Log
import com.task.codematch.data.source.local.dao.UserDao
import com.task.codematch.data.source.local.entity.User
import com.task.codematch.data.source.remote.Resource
import com.task.codematch.data.source.remote.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val usersDao: UserDao,
    private val userService: UserService
) : UserRepository {
    private val LOG_TAG = UsersRepositoryImpl::class.simpleName
    private var isRemoteUsersFetched = false

    /**
     * Fetched the users from network and stored it in database. At the end, data from persistence
     * storage is fetched and emitted.
     */
    override suspend fun getAllUsers(): Flow<Resource<MutableList<User>>> {
        var asFlow = flow {
            emit(Resource.Loading())
            Log.d(LOG_TAG, "isRemoteUsersFetched : " + isRemoteUsersFetched)
            // Emit Database content first
            var localUsers = usersDao.getUsers()
            emit(Resource.Success(localUsers))

            if (!isRemoteUsersFetched) {
                // Fetch latest users from remote
                val remoteUsers = userService.getAllUsers()

                // Check for response validation then save remote users
                if (remoteUsers != null) {
                    isRemoteUsersFetched = true
                    var updatedUsers: MutableList<User> = mutableListOf()
                    for (userDto in remoteUsers) {
                        var user = usersDao.getUserById(userDto.id);
                        if (user != null) {
                            user.name = userDto.name
                            user.username = userDto.username
                            user.email = userDto.email
                            user.phone = userDto.phone
                            user.website = userDto.website
                            user.address = userDto.address
                            user.company = userDto.company
                        } else {
                            user = User(
                                userDto.id,
                                userDto.name,
                                userDto.username,
                                userDto.email,
                                userDto.phone,
                                userDto.website,
                                userDto.address,
                                userDto.company
                            )
                        }
                        usersDao.insertUser(user)
                        updatedUsers.add(user)
                    }
                    emit(Resource.Success(updatedUsers/*usersDao.getUsers()*/))
                } else {
                    emit(Resource.Failed("Something went wrong!"))
                }
            }
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Failed("Network error! Can't get users from remote."))
        }
        return asFlow
    }

    override suspend fun getAllFavoriteUsers(): Flow<Resource<MutableList<User>>> {
        var asFlow = flow {
            emit(Resource.Loading())
            var favoriteUsers = usersDao.getFavoriteUsers()
            emit(Resource.Success(favoriteUsers))
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Failed("Something went wrong!"))
        }
        return asFlow
    }

    override suspend fun saveUser(user: User) = usersDao.insertUser(user)

    /**
     * Retrieves an user with specified [userId].
     * @param userId Unique id of a [User].
     * @return [User] data fetched from the database.
     */
    override suspend fun getUserById(userId: Long): Flow<User> =
        usersDao.getFlowUserById(userId).distinctUntilChanged()

}