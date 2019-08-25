package com.nakharin.marvel.domain

import com.nakharin.marvel.data.api.ApiResponse
import com.nakharin.marvel.data.repository.ApiRepository
import com.nakharin.marvel.presentation.content.model.JsonContent
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Test

class ContentUseCaseTest {

    private val repository: ApiRepository = mock()

    @Test
    fun testContentUseCaseTest() {

        val successResponse = ApiResponse<JsonContent>().apply {
            success = true
        }

        whenever(repository.getContents())
            .thenReturn(Observable.just(successResponse))

        repository.getContents()
            .test()
            .assertComplete()
            .assertValue {
                it.success
            }
    }
}