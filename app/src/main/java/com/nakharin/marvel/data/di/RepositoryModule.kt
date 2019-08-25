package com.nakharin.marvel.data.di

import com.nakharin.marvel.data.repository.ApiRepository
import com.nakharin.marvel.data.source.ContentDataSource
import org.koin.dsl.module

val repositoryModule = module {

    factory { ApiRepository(provideContentDataSource()) }
}

private fun provideContentDataSource(): ContentDataSource {
    return ContentDataSource()
}