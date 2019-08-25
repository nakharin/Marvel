package com.nakharin.marvel

import android.app.Application
import com.nakharin.marvel.data.di.repositoryModule
import com.nakharin.marvel.data.di.useCaseModule
import com.nakharin.marvel.data.di.utilityModule
import com.nakharin.marvel.data.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MarvelApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Method from this class
        setUpKoin()
    }

    private fun setUpKoin() {
        startKoin {
            androidLogger()
            androidContext(this@MarvelApplication)
            modules(
                listOf(
                    repositoryModule,
                    useCaseModule,
                    viewModelModule,
                    utilityModule
                )
            )
        }
    }
}