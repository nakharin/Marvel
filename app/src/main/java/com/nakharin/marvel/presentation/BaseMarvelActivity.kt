package com.nakharin.marvel.presentation

import android.app.Dialog
import android.os.Bundle
import com.bumptech.glide.load.HttpException
import com.emcsthai.pz.utilitylibrary.view.PzDialogConfirmFragment
import com.emcsthai.pz.utilitylibrary.view.PzLoadingDialogView
import com.nakharin.marvel.R
import com.nakharin.marvel.data.api.Constants
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

abstract class BaseMarvelActivity : BasePermissionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initViewModel()
    }

    abstract fun initView()

    abstract fun initViewModel()

    private val pzDialog: PzLoadingDialogView by inject { parametersOf(false) }

    internal fun showLoading() {
        pzDialog.showLoading(supportFragmentManager)
    }

    internal fun setMessageLoading(message: String) {
        pzDialog.setMessage(message)
    }

    internal fun hideLoading() {
        pzDialog.hideLoading()
    }

    internal fun showDialogMessage(title: String, message: String) {
        showDialogMessage(title, message, true, PzDialogConfirmFragment.State.NORMAL)
    }

    internal fun showDialogMessage(title: String, message: String, onOneCallback: ((Dialog) -> Unit)?) {
        showDialogMessage(title, message, false, PzDialogConfirmFragment.State.NORMAL, onOneCallback)
    }

    internal fun showDialogMessage(title: String, message: String, isCancelable: Boolean) {
        showDialogMessage(title, message, isCancelable, PzDialogConfirmFragment.State.NORMAL)
    }

    internal fun showDialogMessage(title: String, message: String, isCancelable: Boolean, onOneCallback: ((Dialog) -> Unit)?) {
        showDialogMessage(title, message, isCancelable, PzDialogConfirmFragment.State.NORMAL, onOneCallback)
    }

    internal fun showDialogMessage(title: String, message: String, state: PzDialogConfirmFragment.State) {
        showDialogMessage(title, message, true, state)
    }

    internal fun showDialogMessage(title: String, message: String, isCancelable: Boolean, state: PzDialogConfirmFragment.State) {
        showDialogMessage(title, message, isCancelable, state, null)
    }

    internal fun showDialogMessage(
        title: String,
        message: String,
        isCancelable: Boolean,
        state: PzDialogConfirmFragment.State,
        onOneCallback: ((Dialog) -> Unit)?
    ) {
        PzDialogConfirmFragment.Builder(supportFragmentManager)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(isCancelable)
            .setState(state)
            .setStrOk(getString(R.string.str_ok))
            .setOnDialogListener { _, dialog ->
                if (onOneCallback != null) {
                    onOneCallback(dialog)
                } else {
                    dialog.dismiss()
                }
            }.build()
    }

    internal fun showDialogMessage(
        title: String,
        message: String,
        isCancelable: Boolean,
        state: PzDialogConfirmFragment.State,
        onOneCallback: ((Dialog) -> Unit)?,
        onTwoCallback: ((Dialog) -> Unit)?
    ) {
        PzDialogConfirmFragment.Builder(supportFragmentManager)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(isCancelable)
            .setState(state)
            .setOnDialogListener(object : PzDialogConfirmFragment.OnTwoDialogListener {
                override fun onPositiveButtonClick(tag: Int, d: Dialog) {
                    if (onOneCallback != null) {
                        onOneCallback(d)
                    }
                }

                override fun onNegativeButtonClick(tag: Int, d: Dialog) {
                    if (onTwoCallback != null) {
                        onTwoCallback(d)
                    }
                }
            }).build()
    }

    internal fun responseAuthenError(error: Throwable) {
        try {
            val httpException = error as HttpException
            if (httpException.statusCode == Constants.AUTHORIZATION_CODE) {
                showDialogMessage(
                    getString(R.string.str_session_expired),
                    getString(R.string.str_please_login_again),
                    false,
                    PzDialogConfirmFragment.State.FAILED
                ) {
                    it.dismiss()
//                    logout()
                }
            } else {
                showDialogMessage(
                    "",
                    "Exception: " + error.localizedMessage,
                    false,
                    PzDialogConfirmFragment.State.NORMAL
                )
            }
        } catch (e: Exception) {
            showDialogMessage(
                "",
                "Exception: " + error.localizedMessage,
                false,
                PzDialogConfirmFragment.State.NORMAL
            )
        }
    }
}
