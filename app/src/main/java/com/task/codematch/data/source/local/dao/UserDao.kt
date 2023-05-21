package com.task.codematch.data.source.local.dao

import androidx.room.*
import com.task.codematch.data.source.local.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Transaction
    @Query("SELECT * FROM User")
    suspend fun getUsers(): MutableList<User>

    @Transaction
    @Query("SELECT * FROM User where isFavorite=1")
    suspend fun getFavoriteUsers(): MutableList<User>

    @Query("SELECT * FROM User WHERE id = :id")
    fun getFlowUserById(id: Long): Flow<User>

    @Query("SELECT * FROM User WHERE id = :id")
    fun getUserById(id: Long): User

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

}
