package com.nakharin.marvel.data.api

import com.google.gson.Gson

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class ApiStatus<out R> {

    object Loading : ApiStatus<Nothing>()
    data class Progress(val bytesRead: Long, val expectedLength: Long) : ApiStatus<Nothing>()
    data class Success<out T>(val data: T) : ApiStatus<T>()
    data class Fail(val message: String) : ApiStatus<Nothing>()
    data class Error(val throwable: Throwable) : ApiStatus<Nothing>()
    object Done : ApiStatus<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Loading -> "Loading"
            is Progress -> "Progress[bytesRead=$bytesRead, expectedLength=$expectedLength]"
            is Success<*> -> "Success[data=${Gson().toJson(data)}]"
            is Fail -> "Fail[message=$message]"
            is Error -> "Error[throwable=${throwable.localizedMessage}]"
            Done -> "Done"
        }
    }
}

/**
 * `true` if [ApiStatus] is of type [Success] & holds non-null [Success.data].
 */
val ApiStatus<*>.successfully
    get() = this is ApiStatus.Success && data != null

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? ApiStatus.Success<T>)?.data ?: fallback
}