package com.nakharin.marvel.utils.extension

import android.view.View
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView

fun ScrollView.smoothScrollToTop() = fullScroll(View.FOCUS_UP)

fun NestedScrollView.smoothScrollToTop() = fullScroll(View.FOCUS_UP)

fun ScrollView.smoothScrollToBottom() = fullScroll(View.FOCUS_DOWN)

fun NestedScrollView.smoothScrollToBottom() = fullScroll(View.FOCUS_DOWN)
