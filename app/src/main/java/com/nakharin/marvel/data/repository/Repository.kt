package com.nakharin.marvel.data.repository

import com.nakharin.marvel.data.api.ResponseWrapper
import com.nakharin.marvel.presentation.content.model.ContentResponse
import io.reactivex.Observable

interface Repository {
    fun getContents(): Observable<ResponseWrapper<ContentResponse>>

    suspend fun getContentsCoroutines(): ResponseWrapper<ContentResponse>
}
