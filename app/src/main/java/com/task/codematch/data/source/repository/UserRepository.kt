package com.task.codematch.data.source.repository

import com.task.codematch.data.source.local.entity.User
import com.task.codematch.data.source.remote.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getAllUsers(): Flow<Resource<List<User>>>

    suspend fun getAllFavoriteUsers(): Flow<Resource<List<User>>>

    suspend fun saveUser(user: User)

    suspend fun getUserById(userId: Long): Flow<User>
}