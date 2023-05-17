package com.task.codematch.data.source.remote

import com.task.codematch.data.source.local.entity.User
import retrofit2.Response
import retrofit2.http.GET

interface UserService {

    @GET("/users")
    fun getAllUsers(): Response<List<User>>
}