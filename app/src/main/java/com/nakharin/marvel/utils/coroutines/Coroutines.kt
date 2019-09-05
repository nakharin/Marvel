package com.nakharin.marvel.utils.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

object Coroutines {

    fun main(scope: suspend () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            scope()
        }
    }

    fun io(scope: suspend () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            scope()
        }
    }

    fun default(scope: suspend () -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            scope()
        }
    }

    @ExperimentalCoroutinesApi
    fun unconfined(scope: suspend () -> Unit) {
        CoroutineScope(Dispatchers.Unconfined).launch {
            scope()
        }
    }
}