package com.nakharin.marvel.presentation.content

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nakharin.marvel.R
import com.nakharin.marvel.base.BaseViewModel
import com.nakharin.marvel.data.api.ApiState
import com.nakharin.marvel.data.api.isSuccessfully
import com.nakharin.marvel.domain.content.ContentUseCase
import com.nakharin.marvel.presentation.content.model.ContentResponse
import com.nakharin.marvel.utils.extension.*
import com.nakharin.marvel.utils.util.CoroutineUtils
import com.nakharin.marvel.utils.glide.UiOnProgressListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContentViewModel(
    private val contentUseCase: ContentUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel() {

    private val _content = MutableLiveData<ApiState<ContentResponse>>()
    val content = _content.toLiveData()

    private val _saveImage = MutableLiveData<ApiState<String>>()
    val saveImage = _saveImage.toLiveData()

    fun fetchContentsAtCoroutines() = viewModelScope.launch(dispatcher) {
        _content.value = ApiState.Loading

        val response = contentUseCase.executeCoroutines()

        _content.value = ApiState.Done
        try {
            response.isSuccessfully({ data ->
                _content.value = ApiState.Success(data)
            }, { message ->
                _content.value = ApiState.Fail(message)
            })
        } catch (e: Exception) {
            e.printStackTrace()
            _content.value = ApiState.Error(Throwable(e))
        }
    }

    fun fetchContentsAtObservable() {
        contentUseCase.execute()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { _content.value = ApiState.Loading }
            .doOnTerminate { _content.value = ApiState.Done }
            .subscribe({ response ->
                response.isSuccessfully({ data ->
                    _content.value = ApiState.Success(data)
                }, { message ->
                    _content.value = ApiState.Fail(message)
                })
            }, { throwable ->
                _content.value = ApiState.Error(throwable)
            })
            .addTo(compositeDisposable)
    }

    private val uiOnProgressListener = object :
        UiOnProgressListener {

        override fun onProgress(bytesRead: Long, expectedLength: Long) {
            _saveImage.value = ApiState.Progress(bytesRead, expectedLength)
        }

        override fun getGranualityPercentage(): Float {
            return 1.0f
        }
    }

    fun saveImage(context: Context, url: String, position: Int) {
        CoroutineUtils.main {
            _saveImage.value = ApiState.Loading
            val bitmap = withContext(Dispatchers.IO) {
                url.getDrawableFromStringUrl().toBitmap()
            }
            val filePath = "${context.getString(R.string.app_name)}_$position.jpg"
            bitmap.saveToGallery(context, filePath, { path ->
                _saveImage.value = ApiState.Success(path)
                _saveImage.value = ApiState.Done
            }, { ex ->
                _saveImage.value = ApiState.Fail(ex.localizedMessage)
                _saveImage.value = ApiState.Done
            })
        }
    }
}
