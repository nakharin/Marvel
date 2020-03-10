package com.nakharin.marvel.domain.content

import com.nakharin.marvel.data.api.ResponseWrapper
import com.nakharin.marvel.data.repository.ApiRepository
import com.nakharin.marvel.presentation.content.model.ContentResponse
import io.reactivex.Observable
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class ContentUseCase(private val apiRepository: ApiRepository) {

    fun execute(): Observable<ResponseWrapper<ContentResponse>> {
        return apiRepository.getContents()
            .delay(2, TimeUnit.SECONDS)
    }

    suspend fun executeCoroutines(): ResponseWrapper<ContentResponse> {
        delay(2000L)
        return apiRepository.getContentsCoroutines()
    }
}
