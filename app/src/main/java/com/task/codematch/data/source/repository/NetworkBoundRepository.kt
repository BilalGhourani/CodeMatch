package com.task.codematch.data.source.repository

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
@ExperimentalCoroutinesApi
abstract class NetworkBoundRepository<RESULT, REQUEST> {

    fun asFlow() = flow<Resource<RESULT>> {

        // Emit Database content first
        emit(Resource.Success(fetchFromLocal()))

        // Fetch latest posts from remote
        val apiResponse = fetchFromRemote()

        // Parse body
        val remotePosts = apiResponse.body()

        // Check for response validation
        if (apiResponse.isSuccessful && remotePosts != null) {
            // Save posts into the persistence storage
            saveRemoteData(remotePosts)
        } else {
            // Something went wrong! Emit Error state.
            emit(Resource.Failed(apiResponse.message()))
        }

        // Retrieve posts from persistence storage and emit
        emit(Resource.Success(fetchFromLocal()))
    }.catch { e ->
        e.printStackTrace()
        emit(Resource.Failed("Network error! Can't get latest posts."))
    }

    /**
     * Saves retrieved from remote into the persistence storage.
     */
    protected abstract suspend fun saveRemoteData(response: REQUEST)

    /**
     * Retrieves all data from persistence storage.
     */
    protected abstract suspend fun fetchFromLocal(): RESULT

    /**
     * Fetches [Response] from the remote end point.
     */
    protected abstract suspend fun fetchFromRemote(): Response<REQUEST>
}