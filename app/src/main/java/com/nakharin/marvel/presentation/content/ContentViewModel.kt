package com.nakharin.marvel.presentation.content

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.nakharin.marvel.GlideApp
import com.nakharin.marvel.MarvelGlideModule
import com.nakharin.marvel.R
import com.nakharin.marvel.UiOnProgressListener
import com.nakharin.marvel.data.api.ApiStatus
import com.nakharin.marvel.domain.content.ContentUseCase
import com.nakharin.marvel.extension.addTo
import com.nakharin.marvel.presentation.BaseViewModel
import com.nakharin.marvel.presentation.content.model.JsonContent
import com.pawegio.kandroid.runAsync
import com.pawegio.kandroid.runOnUiThread
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream

class ContentViewModel(private val contentUseCase: ContentUseCase) : BaseViewModel<JsonContent>() {

    private val contentStatus = MutableLiveData<ApiStatus<JsonContent>>()
    private val saveStatus = MutableLiveData<ApiStatus<String>>()

    fun contentStatus(): LiveData<ApiStatus<JsonContent>> = contentStatus
    fun saveStatus(): LiveData<ApiStatus<String>> = saveStatus

    fun getContents() {
        contentUseCase.execute()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { contentStatus.value = ApiStatus.Loading }
            .doOnTerminate { contentStatus.value = ApiStatus.Done }
            .subscribe({
                if (it.success && it.data != null) {
                    contentStatus.value = ApiStatus.Success(it.data!!)

                } else {
                    contentStatus.value = ApiStatus.Fail(it.message)
                }
            }, {
                contentStatus.value = ApiStatus.Error(it)
            })
            .addTo(compositeDisposable)
    }

    private val uiOnProgressListener = object : UiOnProgressListener {

        override fun onProgress(bytesRead: Long, expectedLength: Long) {
            saveStatus.value = ApiStatus.Progress(bytesRead, expectedLength)
        }

        override fun getGranualityPercentage(): Float {
            return 1.0f
        }
    }

    fun saveImage(context: Context, url: String, position: Int) {
        saveStatus.value = ApiStatus.Loading

        runAsync {
            val requestOptions = RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)

            MarvelGlideModule.expect(url, uiOnProgressListener)

            val bitmap = GlideApp.with(context)
                .asBitmap()
                .load(url)
                .apply(requestOptions)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        MarvelGlideModule.forget(url)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        MarvelGlideModule.forget(url)
                        return false
                    }
                })
                .submit()
                .get()

            val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString()
            val myDir = File(root)
            myDir.mkdirs()
            val file = File(myDir, "${context.getString(R.string.app_name)}_$position.jpg")
            if (file.exists())
                file.delete()
            try {
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
                saveStatus.value = ApiStatus.Fail(e.localizedMessage)
                saveStatus.value = ApiStatus.Done
            }

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(
                context,
                arrayOf(file.toString()),
                null
            ) { path, _ ->
                runOnUiThread {
                    saveStatus.value = ApiStatus.Success(path)
                    saveStatus.value = ApiStatus.Done
                }
            }
        }
    }
}