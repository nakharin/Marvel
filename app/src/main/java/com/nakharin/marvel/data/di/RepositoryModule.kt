package com.nakharin.marvel.data.di

import com.nakharin.marvel.data.repository.ApiRepository
import com.nakharin.marvel.data.source.ContentLocalDataSource
import org.koin.dsl.module

val repositoryModule = module {

    single { ApiRepository(provideContentDataSource()) }
}

private fun provideContentDataSource(): ContentLocalDataSource {
    return ContentLocalDataSource()
}
