package com.nakharin.marvel.data.di

import com.nakharin.marvel.presentation.content.ContentViewModel
import org.koin.dsl.module

val viewModelModule = module {

    factory { ContentViewModel(get()) }
}