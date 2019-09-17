package com.nakharin.marvel.utils.extension

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

fun Drawable.toBitmap(): Bitmap {
    return (this as BitmapDrawable).bitmap
}