package com.task.codematch.data.source.local.dao

import androidx.room.*
import com.task.codematch.data.source.local.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Transaction
    @Query("SELECT * FROM User")
    suspend fun getUsers(): List<User>

    @Query("SELECT * FROM User WHERE id = :id")
    fun getUserById(id: Long): Flow<User>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Transaction
    @Update()
    suspend fun updateUser(user: User)

    @Transaction
    @Delete()
    suspend fun deleteUser(user: User)
}
