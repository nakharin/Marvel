package com.nakharin.marvel.presentation

import android.app.Dialog
import com.bumptech.glide.load.HttpException
import com.emcsthai.pz.utilitylibrary.view.PzDialogConfirmFragment
import com.nakharin.marvel.R
import com.nakharin.marvel.data.api.Constants

abstract class BaseMarvelActivity : BasePermissionActivity() {

    fun showDialogMessage(title: String, message: String) {
        showDialogMessage(title, message, true, PzDialogConfirmFragment.State.NORMAL)
    }

    fun showDialogMessage(title: String, message: String, onOneCallback: ((Dialog) -> Unit)?) {
        showDialogMessage(title, message, false, PzDialogConfirmFragment.State.NORMAL, onOneCallback)
    }

    fun showDialogMessage(title: String, message: String, isCancelable: Boolean) {
        showDialogMessage(title, message, isCancelable, PzDialogConfirmFragment.State.NORMAL)
    }

    fun showDialogMessage(title: String, message: String, isCancelable: Boolean, onOneCallback: ((Dialog) -> Unit)?) {
        showDialogMessage(title, message, isCancelable, PzDialogConfirmFragment.State.NORMAL, onOneCallback)
    }

    fun showDialogMessage(title: String, message: String, state: PzDialogConfirmFragment.State) {
        showDialogMessage(title, message, true, state)
    }

    fun showDialogMessage(title: String, message: String, isCancelable: Boolean, state: PzDialogConfirmFragment.State) {
        showDialogMessage(title, message, isCancelable, state, null)
    }

    fun showDialogMessage(
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

    fun showDialogMessage(
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

    fun responseAuthenError(error: Throwable) {
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