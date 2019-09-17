package com.nakharin.marvel

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.util.CoilLogger
import coil.util.CoilUtils
import com.nakharin.marvel.data.di.repositoryModule
import com.nakharin.marvel.data.di.useCaseModule
import com.nakharin.marvel.data.di.utilityModule
import com.nakharin.marvel.data.di.viewModelModule
import com.nakharin.marvel.utils.extension.forceTls12
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MarvelApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Method from this class
        setUpKoin()

        // Method from this class
        setUpCoil()
    }

    private fun setUpKoin() {
        startKoin {
            androidLogger()
            androidContext(this@MarvelApplication)
            modules(
                listOf(
                    repositoryModule,
                    useCaseModule,
                    viewModelModule,
                    utilityModule
                )
            )
        }
    }


    private fun setUpCoil() {
        CoilLogger.setEnabled(true) // Enable logging to the standard Android log.

        val imageLoader = ImageLoader(applicationContext) {
            availableMemoryPercentage(0.5) // Use 50% of the application's available memory.
            bitmapPoolPercentage(0.5) // Use 50% of the memory allocated to this ImageLoader for the bitmap pool.
            allowHardware(true)
            crossfade(true) // Show a short crossfade when loading images from network or disk into an ImageView.
            okHttpClient {
                // Lazily create the OkHttpClient that is used for network operations.

                OkHttpClient.Builder()
//                    .addInterceptor { chain ->
//                        val originalRequest = chain.request()
//
//                        val authorisedRequest = originalRequest.newBuilder()
//                            .header(Constants.AUTHORIZATION, "Bearer ${tokenPreference.getAccessToken()}")
//                            .build()
//
//                        chain.proceed(authorisedRequest)
//                    }
                    .cache(CoilUtils.createDefaultCache(applicationContext))
                    .forceTls12() // The Unsplash API requires TLS 1.2, which isn't enabled by default before Lollipop.
                    .build()
            }
        }

        Coil.setDefaultImageLoader(imageLoader)
    }
}