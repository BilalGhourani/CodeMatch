package com.task.codematch.data.source.repository

import com.task.codematch.data.model.UserModel
import com.task.codematch.data.source.remote.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import retrofit2.Response

/**
 * A repository which provides resource from local database as well as remote end point.
 *
 * [RESULT] represents the type for database.
 * [REQUEST] represents the type for network.
 */
abstract class NetworkBoundRepository<RESULT, REQUEST> {

    fun asFlow() = flow<Resource<RESULT>> {

        emit(Resource.Loading())

        // Emit Database content first
        var localUsers = fetchFromLocal()
        emit(Resource.Success(localUsers))

        // Fetch latest posts from remote
        val remotePosts = fetchFromRemote()

        // Check for response validation
        if (remotePosts != null) {
            saveRemoteData(remotePosts)
        } else {
            emit(Resource.Failed("Something went wrong!"))
        }

        emit(Resource.Success(fetchFromLocal()))
    }.catch { e ->
        e.printStackTrace()
        emit(Resource.Failed("Network error! Can't get latest posts."))
    }

    /**
     * Saves retrieved users from remote into the persistence storage.
     */
    protected abstract suspend fun saveRemoteData(response: MutableList<UserModel>)

    /**
     * Retrieves all users from persistence storage.
     */
    protected abstract suspend fun fetchFromLocal(): RESULT

    /**
     * Fetches [Response] from the remote end point.
     */
    protected abstract suspend fun fetchFromRemote(): MutableList<UserModel>
}