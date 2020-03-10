package com.nakharin.marvel.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nakharin.marvel.utils.delegate.SingleMutableLiveData
import com.nakharin.marvel.utils.extension.toLiveData
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _showLoading = SingleMutableLiveData<Boolean>()
    val showLoading = _showLoading.toLiveData()

    open fun postShowLoading() = viewModelScope.launch(dispatcher) {
        _showLoading.value = true
    }

    open fun postDismissLoading() = viewModelScope.launch(dispatcher) {
        _showLoading.value = false
    }

    val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
