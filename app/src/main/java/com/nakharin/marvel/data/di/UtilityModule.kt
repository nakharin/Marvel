package com.nakharin.marvel.data.di

import com.emcsthai.pz.utilitylibrary.view.PzLoadingDialogView
import org.koin.dsl.module

val utilityModule = module {
    factory { (isCancelable: Boolean) -> providePzLoadingDialogView(isCancelable) }
}

private fun providePzLoadingDialogView(isCancelable: Boolean): PzLoadingDialogView {
    return PzLoadingDialogView.newInstance(isCancelable)
}
