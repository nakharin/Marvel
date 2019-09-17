package com.nakharin.marvel.presentation.content

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nakharin.marvel.R
import com.nakharin.marvel.data.api.ApiState
import com.nakharin.marvel.data.api.isSuccessfully
import com.nakharin.marvel.domain.content.ContentUseCase
import com.nakharin.marvel.presentation.BaseViewModel
import com.nakharin.marvel.presentation.content.model.JsonContent
import com.nakharin.marvel.utils.coroutines.Coroutines
import com.nakharin.marvel.utils.extension.addTo
import com.nakharin.marvel.utils.extension.getDrawableFromStringUrl
import com.nakharin.marvel.utils.extension.saveToGallery
import com.nakharin.marvel.utils.extension.toBitmap
import com.nakharin.marvel.utils.glide.UiOnProgressListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContentViewModel(private val contentUseCase: ContentUseCase) : BaseViewModel() {

    private val contentState = MutableLiveData<ApiState<JsonContent>>()
    private val saveImageState = MutableLiveData<ApiState<String>>()

    fun getContentState(): LiveData<ApiState<JsonContent>> = contentState
    fun getSaveImageState(): LiveData<ApiState<String>> = saveImageState

    fun fetchContentsAtCoroutines() {
        Coroutines.main {
            contentState.value = ApiState.Loading
            val response = withContext(Dispatchers.IO) {
                contentUseCase.executeCoroutines()
            }
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

    fun fetchContentsAtObservable() {
        contentUseCase.execute()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { contentState.value = ApiState.Loading }
            .doOnTerminate { contentState.value = ApiState.Done }
            .subscribe({ response ->
                response.isSuccessfully({ data ->
                    contentState.value = ApiState.Success(data)
                }, { message ->
                    contentState.value = ApiState.Fail(message)
                })
            }, { throwable ->
                contentState.value = ApiState.Error(throwable)
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
        Coroutines.main {
            saveImageState.value = ApiState.Loading
            val bitmap = withContext(Dispatchers.IO) {
                url.getDrawableFromStringUrl().toBitmap()
            }
            val filePath = "${context.getString(R.string.app_name)}_$position.jpg"
            bitmap.saveToGallery(context, filePath, { path ->
                saveImageState.value = ApiState.Success(path)
                saveImageState.value = ApiState.Done
            }, { ex ->
                saveImageState.value = ApiState.Fail(ex.localizedMessage)
                saveImageState.value = ApiState.Done
            })
        }
    }
}