package com.nakharin.marvel.data.repository

import com.nakharin.marvel.data.api.ApiResponse
import com.nakharin.marvel.presentation.content.model.JsonContent
import io.reactivex.Observable

interface Repository {
    fun getContents(): Observable<ApiResponse<JsonContent>>

    suspend fun getContentsCoroutines(): ApiResponse<JsonContent>
}
