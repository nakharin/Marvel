package com.nakharin.marvel.base

import kotlinx.coroutines.*

abstract class BaseUseCase<in P, R>(
    private val errorMapper: BaseErrorMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    @Throws(Exception::class)
    protected abstract suspend fun execute(params: P): R

    /**
     * Be careful
     * this method can throw exception
     */
    @Throws(Exception::class)
    suspend fun forceExecute(params: P): R {
        return execute(params)
    }

    open suspend fun invoke(
        params: P
    ): ResultWrapper<R> {
        return try {
            val response = withContext(dispatcher) {
                execute(params)
            }
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Failure(errorMapper.transform(e))
        }
    }

    // for call from java class
    fun runJob(
        params: P,
        resumeWith: ((ResultWrapper<R>) -> Unit)?
    ): Job = GlobalScope.launch(Dispatchers.Main) {
        val result = invoke(params)
        resumeWith?.invoke(result)
    }
}
