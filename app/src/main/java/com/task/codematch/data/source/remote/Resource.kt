package com.task.codematch.data.source.remote


sealed class Resource<T> {

    class Success<T>(val data: T) : Resource<T>()
    class Failed<T>(val message: String) : Resource<T>()
    class Loading<T>(val data: Any?) : Resource<T>()
}