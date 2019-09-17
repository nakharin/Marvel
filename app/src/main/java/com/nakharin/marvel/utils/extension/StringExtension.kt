package com.nakharin.marvel.utils.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import coil.Coil
import coil.api.get
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.nakharin.marvel.GlideApp
import com.nakharin.marvel.MarvelGlideModule
import com.nakharin.marvel.utils.glide.UiOnProgressListener

suspend fun String.getBitmapFromStringUrl(
    context: Context,
    listener: UiOnProgressListener
): Bitmap {

    val requestOptions = RequestOptions()
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)

    MarvelGlideModule.expect(this, listener)

    return GlideApp.with(context)
        .asBitmap()
        .load(this)
        .apply(requestOptions)
        .listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                MarvelGlideModule.forget(this@getBitmapFromStringUrl)
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                MarvelGlideModule.forget(this@getBitmapFromStringUrl)
                return false
            }
        })
        .submit()
        .get()
}

suspend fun String.getDrawableFromStringUrl(): Drawable {
    return Coil.get(this)
}

