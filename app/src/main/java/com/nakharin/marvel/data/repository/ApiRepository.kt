package com.nakharin.marvel.data.repository

import com.nakharin.marvel.data.api.ApiResponse
import com.nakharin.marvel.data.source.ContentLocalDataSource
import com.nakharin.marvel.presentation.content.model.JsonContent
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiRepository(
    private val contentLocalDataSource: ContentLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : Repository {

    override fun getContents(): Observable<ApiResponse<JsonContent>> {
        return Observable.just(contentLocalDataSource.generate())
    }

    override suspend fun getContentsCoroutines(): ApiResponse<JsonContent> {
        return withContext(dispatcher) {
            contentLocalDataSource.generate()
        }
    }
}
