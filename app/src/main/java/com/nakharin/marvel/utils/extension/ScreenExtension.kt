package com.nakharin.marvel.utils.extension

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.roundToInt

fun Float.dpToPx(): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    Resources.getSystem().displayMetrics
).roundToInt()

fun Int.dpToPx(): Int = toFloat().dpToPx()

fun Double.dpToPx(): Int = toFloat().dpToPx()

fun Long.dpToPx(): Int = toFloat().dpToPx()
