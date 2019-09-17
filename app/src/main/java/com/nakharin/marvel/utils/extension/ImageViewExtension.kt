package com.nakharin.marvel.utils.extension

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewTreeObserver.OnPreDrawListener
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.api.load
import coil.request.CachePolicy
import com.nakharin.marvel.R

/*******************************************************************************************
 ************************************ Private Method ***************************************
 *******************************************************************************************/

// https://proandroiddev.com/replace-progressdialog-with-a-progress-button-in-your-app-14ed1d50b44
fun provideCircularProgressDrawable(
    context: Context,
    width: Int
): CircularProgressDrawable {
    var strokeWidth = (width / 30).toFloat()
    var centerRadius = (width / 15).toFloat()

    strokeWidth = if (strokeWidth > 20) {
        20f
    } else {
        strokeWidth
    }

    centerRadius = if (centerRadius > 100) {
        100f
    } else {
        centerRadius
    }

    if (strokeWidth == 0f) {
        strokeWidth = 20f
    }

    if (centerRadius == 0f) {
        centerRadius = 100f
    }

    return CircularProgressDrawable(context).apply {
        this.strokeWidth = strokeWidth
        this.centerRadius = centerRadius
        this.setColorSchemeColors(ContextCompat.getColor(context, R.color.colorAccentYellow))
    }
}

private fun loadImage(
    imageView: ImageView,
    url: String,
    isSkipMemoryCache: Boolean,
    isReSize: Boolean
) {
    val vto = imageView.viewTreeObserver
    vto.addOnPreDrawListener(object : OnPreDrawListener {
        @SuppressLint("CheckResult")
        override fun onPreDraw(): Boolean {
            imageView.viewTreeObserver.removeOnPreDrawListener(this)
            val width = imageView.measuredHeight
            val height = imageView.measuredWidth

            val circularProgressDrawable = provideCircularProgressDrawable(imageView.context, width)

            imageView.load(url) {
                placeholder(circularProgressDrawable)
                error(R.drawable.ic_error)

                if (isReSize) {
                    size(width, height)
                }

                if (isSkipMemoryCache) {
                    memoryCachePolicy(CachePolicy.DISABLED)
                }

                listener({
                    circularProgressDrawable.start()
                }, {
                    circularProgressDrawable.stop()
                }, { _, throwable ->
                    throwable.printStackTrace()
                    circularProgressDrawable.stop()
                }, { _, _ ->
                    circularProgressDrawable.stop()
                })
            }

            return true
        }
    })
}

/*******************************************************************************************
 ************************************ Public Method ***************************************
 *******************************************************************************************/

fun ImageView.loadImage(url: String) {
    loadImage(imageView = this, url = url, isSkipMemoryCache = false, isReSize = true)
}
