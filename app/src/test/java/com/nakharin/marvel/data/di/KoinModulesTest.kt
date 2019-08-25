package com.nakharin.marvel.data.di

import android.app.Application
import android.content.Context
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

class KoinModulesTest : KoinTest {

    @Test
    fun testKoinModules() {

        val mockedAndroid = module {
            single { mock<Context>() }
            single { mock<Application>() }
        }

        val a = startKoin {
            modules(
                listOf(
                    useCaseModule,
                    utilityModule,
                    repositoryModule,
                    mockedAndroid
                )
            )
        }

        getKoin()
    }
}