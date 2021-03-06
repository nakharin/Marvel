package com.nakharin.marvel.presentation

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nakharin.marvel.data.api.ApiState
import com.nakharin.marvel.data.source.ContentLocalDataSource
import com.nakharin.marvel.domain.content.ContentUseCase
import com.nakharin.marvel.presentation.content.ContentViewModel
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ContentViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val context: Context = mock()

    private val useCase: ContentUseCase = mock()

    private lateinit var viewModel: ContentViewModel

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }

        viewModel = ContentViewModel(useCase)
    }

    @After
    fun clean() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun testLoadData_hasData_ShouldReturnList() {

        val dataSource = ContentLocalDataSource()
        val response = dataSource.generate()

        whenever(useCase.execute())
            .thenReturn(Observable.just(response))

        viewModel.fetchContentsAtObservable()

        assert(viewModel.getContentState().value != ApiState.Fail(""))
        assert(viewModel.getContentState().value != ApiState.Error(Throwable("")))
    }

    @Test
    fun testGetImageListShouldReturnList() {
        val dataSource = ContentLocalDataSource()
        val response = dataSource.generate()
        whenever(useCase.execute())
            .thenReturn(Observable.just(response))

        useCase.execute()
            .test()
            .assertComplete()
            .assertValue {
                it.data?.images!!.isNotEmpty()
            }
    }

    @Test
    fun testSaveImage_hasData_ShouldReturnSuccess() {

        val dataSource = ContentLocalDataSource()
        val response = dataSource.generate()
        val url = response.data!!.images[0]

        doReturn(ApiState.Success("aaaaa"))

        viewModel.saveImage(context, url, 0)

        assert(viewModel.getSaveImageState().value != ApiState.Fail(""))
        assert(viewModel.getSaveImageState().value != ApiState.Error(Throwable("")))
    }
}
