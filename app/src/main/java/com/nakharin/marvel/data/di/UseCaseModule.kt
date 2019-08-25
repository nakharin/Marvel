package com.nakharin.marvel.data.di

import com.nakharin.marvel.domain.main.MainUseCase
import org.koin.dsl.module

val useCaseModule = module {

    factory { MainUseCase(get()) }
}