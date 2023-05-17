package com.task.codematch.data.source.remote

import com.task.codematch.data.source.local.entity.User
import retrofit2.http.GET

interface UserService {

    @GET("/users")
   suspend fun getAllUsers(): List<User>
}