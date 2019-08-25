package com.nakharin.marvel.data.repository

import com.nakharin.marvel.data.api.ApiResponse
import com.nakharin.marvel.data.source.ContentDataSource
import com.nakharin.marvel.presentation.content.model.JsonContent
import io.reactivex.Observable

class ApiRepository(private val dataSource: ContentDataSource) : Repository {

    fun getContents(): Observable<ApiResponse<JsonContent>> {
        return Observable.just(dataSource.generate())
    }
}