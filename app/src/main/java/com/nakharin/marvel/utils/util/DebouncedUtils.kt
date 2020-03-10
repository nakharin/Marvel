package com.nakharin.marvel.utils.util

import android.os.SystemClock

object DebouncedUtils {

    private const val DEFAULT_ACTION_DEBOUNCE_THRESHOLD_MS = 1000L

    private var lastActionMillis: Long = 0

    fun action(onAction: () -> Unit) {
        val now = SystemClock.elapsedRealtime()
        if (now - lastActionMillis > DEFAULT_ACTION_DEBOUNCE_THRESHOLD_MS) {
            onAction()
        }

        lastActionMillis = now
    }
}
