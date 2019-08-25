package com.nakharin.marvel.data.repository

import com.nakharin.marvel.data.source.ContentDataSource
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Test

class ApiRepositoryTest {

    private val repository: ApiRepository = mock()

    @Test
    fun test_GetContent_Should_Return_Success() {

        val dataSource = ContentDataSource()
        val response = dataSource.generate()

        whenever(repository.getContents())
            .thenReturn(Observable.just(response))

        repository.getContents()
            .test()
            .assertComplete()
            .assertValue {
                it.success
            }
    }

    @Test
    fun test_GetContent_Should_Return_Fail() {

        val errorMessage = "404 Data Not found"

        whenever(repository.getContents())
            .thenReturn(Observable.error(Throwable(errorMessage)))

        repository.getContents()
            .test()
            .assertError {
                it.localizedMessage == errorMessage
            }
    }
}