package com.nakharin.marvel.presentation.content

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.emcsthai.pz.utilitylibrary.view.PzLoadingDialogView
import com.nakharin.marvel.R
import com.nakharin.marvel.data.api.ApiState
import com.nakharin.marvel.presentation.BaseMarvelActivity
import com.nakharin.marvel.presentation.content.adapter.ContentAdapter
import com.nakharin.marvel.presentation.content.model.JsonContent
import com.pawegio.kandroid.toast
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ContentActivity : BaseMarvelActivity() {

    private val viewModel: ContentViewModel by viewModel()

    private val pzDialog: PzLoadingDialogView by inject { parametersOf(false) }

    private val contentAdapter: ContentAdapter = ContentAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpView()

        viewModel.getContentsCoroutines()

        viewModel.contentState().observe(this, contentObserver)
        viewModel.saveImageState().observe(this, saveImageObserver)

        contentAdapter.setOnItemClickLister(onItemClickListener)
    }

    private fun setUpView() {
        mainRcvContents.adapter = contentAdapter
    }

    private fun showLoading() {
        pzDialog.showLoading(supportFragmentManager)
    }

    private fun hideLoading() {
        pzDialog.hideLoading()
    }

    /************************************* Observer *********************************************/

    private val contentObserver = Observer<ApiState<JsonContent>> {
        when (it) {
            ApiState.Loading -> showLoading()
            is ApiState.Success -> {
                mainTxtTitle.text = it.data.title
                contentAdapter.addAllItem(it.data.images)
            }
            is ApiState.Fail -> showDialogMessage("", it.message)
            is ApiState.Error -> showDialogMessage("", it.throwable.localizedMessage)
            ApiState.Done -> hideLoading()
        }
    }

    private val saveImageObserver = Observer<ApiState<String>> {
        when (it) {
            ApiState.Loading -> showLoading()
            is ApiState.Progress -> {
                val downloading = it.bytesRead / 1024
                val maxSize = it.expectedLength / 1024
                pzDialog.setMessage("$downloading/$maxSize kb downloading...")
            }
            is ApiState.Success -> toast(getString(R.string.str_save_completed))
            is ApiState.Fail -> showDialogMessage("", it.message)
            is ApiState.Error -> showDialogMessage("", it.throwable.localizedMessage)
            ApiState.Done -> hideLoading()
        }
    }

    /************************************* Listener *********************************************/

    private val onItemClickListener: (View, String, Int) -> Unit = { view, url, position ->
        val permissions = mutableListOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        checkPermissionsGranted(permissions) {
            viewModel.saveImage(view.context, url, position)
        }
    }
}
