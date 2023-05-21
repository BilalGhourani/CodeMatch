package com.task.codematch.data.source.remote

import com.task.codematch.data.model.UserDTO
import retrofit2.http.GET

interface UserService {

    @GET("/users")
   suspend fun getAllUsers(): MutableList<UserDTO>
}