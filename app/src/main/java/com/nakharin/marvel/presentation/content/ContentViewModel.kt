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
import com.nakharin.marvel.data.api.ApiState
import com.nakharin.marvel.data.api.isSuccessfully
import com.nakharin.marvel.domain.content.ContentUseCase
import com.nakharin.marvel.presentation.BaseViewModel
import com.nakharin.marvel.presentation.content.model.JsonContent
import com.nakharin.marvel.utils.coroutines.Coroutines
import com.nakharin.marvel.utils.extension.addTo
import com.nakharin.marvel.utils.glide.UiOnProgressListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream

class ContentViewModel(private val contentUseCase: ContentUseCase) : BaseViewModel() {

    private val contentState = MutableLiveData<ApiState<JsonContent>>()
    private val saveImageState = MutableLiveData<ApiState<String>>()

    fun getContentState(): LiveData<ApiState<JsonContent>> = contentState
    fun getSaveImageState(): LiveData<ApiState<String>> = saveImageState

    fun fetchContentsAtCoroutines() {
        contentState.value = ApiState.Loading
        Coroutines.io {
            val response = contentUseCase.executeCoroutines()
            Coroutines.main {
                contentState.value = ApiState.Done
                try {
                    response.isSuccessfully({ data ->
                        contentState.value = ApiState.Success(data)
                    }, { message ->
                        contentState.value = ApiState.Fail(message)
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    contentState.value = ApiState.Error(Throwable(e))
                }
            }
        }
    }

    fun fetchContentsAtObservable() {
        contentUseCase.execute()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { contentState.value = ApiState.Loading }
            .doOnTerminate { contentState.value = ApiState.Done }
            .subscribe({
                it.isSuccessfully({ data ->
                    contentState.value = ApiState.Success(data)
                }, { message ->
                    contentState.value = ApiState.Fail(message)
                })
            }, {
                contentState.value = ApiState.Error(it)
            })
            .addTo(compositeDisposable)
    }

    private val uiOnProgressListener = object :
        UiOnProgressListener {

        override fun onProgress(bytesRead: Long, expectedLength: Long) {
            saveImageState.value = ApiState.Progress(bytesRead, expectedLength)
        }

        override fun getGranualityPercentage(): Float {
            return 1.0f
        }
    }

    fun saveImage(context: Context, url: String, position: Int) {
        saveImageState.value = ApiState.Loading

        Coroutines.io {
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

            if (file.exists()) {
                file.delete()
            }

            try {
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
                Coroutines.main {
                    saveImageState.value = ApiState.Fail(e.localizedMessage)
                    saveImageState.value = ApiState.Done
                }
            }

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(
                context,
                arrayOf(file.toString()),
                null
            ) { path, _ ->
                Coroutines.main {
                    saveImageState.value = ApiState.Success(path)
                    saveImageState.value = ApiState.Done
                }
            }
        }
    }
}