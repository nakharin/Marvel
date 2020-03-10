package com.nakharin.marvel.domain

import com.nakharin.marvel.data.source.ContentLocalDataSource
import com.nakharin.marvel.domain.content.ContentUseCase
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Test

class ContentUseCaseTest {

    private val useCase: ContentUseCase = mock()

    @Test
    fun test_ContentUseCase_Should_Return_Success() {

        val dataSource = ContentLocalDataSource()
        val response = dataSource.generate()

        whenever(useCase.execute())
            .thenReturn(Observable.just(response))

        useCase.execute()
            .test()
            .assertComplete()
            .assertValue {
                it.success
            }
    }

    @Test
    fun test_ContentUseCase_Should_Return_Fail() {
        val errorMessage = "404 Data Not found"

        whenever(useCase.execute())
            .thenReturn(Observable.error(Throwable(errorMessage)))

        useCase.execute()
            .test()
            .assertError {
                it.localizedMessage == errorMessage
            }
    }
}
