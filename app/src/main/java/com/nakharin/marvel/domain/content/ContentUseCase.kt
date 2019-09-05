package com.nakharin.marvel.domain.content

import com.nakharin.marvel.data.api.ApiResponse
import com.nakharin.marvel.data.repository.ApiRepository
import com.nakharin.marvel.domain.BaseUseCase
import com.nakharin.marvel.presentation.content.model.JsonContent
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class ContentUseCase(private val apiRepository: ApiRepository) : BaseUseCase() {

    fun execute(): Observable<ApiResponse<JsonContent>> {
        return apiRepository.getContents()
            .delay(2, TimeUnit.SECONDS)
    }

    suspend fun executeCoroutines(): ApiResponse<JsonContent> {
        return apiRepository.getContentsCoroutines()
    }
}