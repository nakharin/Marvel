package com.nakharin.marvel.data.di

import com.nakharin.marvel.domain.content.ContentUseCase
import org.koin.dsl.module

val useCaseModule = module {

    factory { ContentUseCase(get()) }
}