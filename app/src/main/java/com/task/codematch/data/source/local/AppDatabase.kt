package com.task.codematch.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.task.codematch.data.source.local.dao.UserDao
import com.task.codematch.data.source.local.entity.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun UserDao(): UserDao
}