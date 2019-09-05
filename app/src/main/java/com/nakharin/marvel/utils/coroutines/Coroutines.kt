package com.nakharin.marvel.utils.coroutines

import kotlinx.coroutines.*

object Coroutines {

    val Main = Dispatchers.Main
    val IO = Dispatchers.IO
    val Default = Dispatchers.Default
    @ExperimentalCoroutinesApi
    val Unconfined = Dispatchers.Unconfined

    fun launch(scope: suspend () -> Unit) {
        runBlocking {
            launch {
                scope()
            }
        }
    }

    fun main(scope: suspend () -> Unit) {
        CoroutineScope(Main).launch {
            scope()
        }
    }

    fun io(scope: suspend () -> Unit) {
        CoroutineScope(IO).launch {
            scope()
        }
    }

    fun default(scope: suspend () -> Unit) {
        CoroutineScope(Default).launch {
            scope()
        }
    }

    @ExperimentalCoroutinesApi
    fun unconfined(scope: suspend () -> Unit) {
        CoroutineScope(Unconfined).launch {
            scope()
        }
    }
}