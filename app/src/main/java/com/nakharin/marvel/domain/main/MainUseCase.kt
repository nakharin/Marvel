package com.nakharin.marvel.domain.main

import com.nakharin.marvel.data.api.ApiResponse
import com.nakharin.marvel.data.repository.ApiRepository
import com.nakharin.marvel.presentation.content.model.JsonContent
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class MainUseCase(private val apiRepository: ApiRepository) {

    fun execute(): Observable<ApiResponse<JsonContent>> {
        return apiRepository.getContents()
            .delay(2, TimeUnit.SECONDS)
    }
}