package com.nakharin.marvel.base

sealed class ResultWrapper<out T> {

    data class Success<out T>(val data: T) : ResultWrapper<T>()

    data class Failure(val errorModel: BaseErrorModel) : ResultWrapper<Nothing>()

}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val ResultWrapper<*>.isSuccess
    get() = this is ResultWrapper.Success

val ResultWrapper<*>.isSuccessNonNull get() = isSuccess && successOr(null) != null

fun <T> ResultWrapper<T>.successOr(fallback: T): T {
    return (this as? ResultWrapper.Success<T>)?.data ?: fallback
}

fun <T> ResultWrapper<T>.success(): T {
    return (this as ResultWrapper.Success<T>).data
}

fun <Nothing> ResultWrapper<Nothing>.errorModel(): BaseErrorModel {
    return (this as ResultWrapper.Failure).errorModel
}
