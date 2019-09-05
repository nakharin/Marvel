package com.nakharin.marvel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.nakharin.marvel.utils.glide.DispatchingProgressListener
import com.nakharin.marvel.utils.glide.OkHttpProgressResponseBody
import com.nakharin.marvel.utils.glide.ResponseProgressListener
import com.nakharin.marvel.utils.glide.UiOnProgressListener
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit

// https://medium.com/@mr.johnnyne/how-to-use-glide-v4-load-image-with-progress-update-eb02671dac18
@GlideModule
class MarvelGlideModule : AppGlideModule() {

    companion object {
        fun forget(url: String) {
            DispatchingProgressListener.forget(url)
        }

        fun expect(url: String, listener: UiOnProgressListener) {
            DispatchingProgressListener.expect(url, listener)
        }
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val memoryCacheSizeBytes = 100 * 1024 * 1024 // 100mb
        builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes.toLong()))
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, memoryCacheSizeBytes.toLong()))

        val requestOptions = RequestOptions()
            .signature(ObjectKey(System.currentTimeMillis() / (24 * 60 * 60 * 1000)))
            .encodeFormat(Bitmap.CompressFormat.PNG)
            .encodeQuality(100)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .format(DecodeFormat.PREFER_ARGB_8888)
            .skipMemoryCache(false)
        builder.setDefaultRequestOptions(requestOptions)

        if (BuildConfig.DEBUG) {
            builder.setLogLevel(Log.DEBUG)
        }
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addNetworkInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)

                val listener: ResponseProgressListener =
                    DispatchingProgressListener()

                val okHttpProgressResponseBody =
                    OkHttpProgressResponseBody(
                        request.url(),
                        response.body()!!,
                        listener
                    )

                response.newBuilder()
                    .body(okHttpProgressResponseBody).build()
            }
            .build()

        val factory = OkHttpUrlLoader.Factory(client)

        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }
}