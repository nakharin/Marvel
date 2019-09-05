package com.nakharin.marvel.utils.coroutines

import kotlinx.coroutines.*

object Coroutines {

    fun main(scope: suspend () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            scope()
        }
    }

    fun IO(scope: suspend () -> Unit) {
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