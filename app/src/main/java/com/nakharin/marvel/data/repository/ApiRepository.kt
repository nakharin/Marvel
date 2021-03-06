package com.nakharin.marvel.data.repository

import com.nakharin.marvel.data.api.ResponseWrapper
import com.nakharin.marvel.data.source.ContentLocalDataSource
import com.nakharin.marvel.presentation.content.model.ContentResponse
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiRepository(
    private val contentLocalDataSource: ContentLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : Repository {

    override fun getContents(): Observable<ResponseWrapper<ContentResponse>> {
        return Observable.just(contentLocalDataSource.generate())
    }

    override suspend fun getContentsCoroutines(): ResponseWrapper<ContentResponse> {
        return withContext(dispatcher) {
            contentLocalDataSource.generate()
        }
    }
}
