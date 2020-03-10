package com.nakharin.marvel.base

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import java.net.ConnectException
import java.net.HttpURLConnection
import retrofit2.HttpException

open class BaseErrorMapper(
    open val context: Context,
    val gson: Gson
) {

    protected var errorResponse: BaseErrorResponse? = null

    protected fun getErrorResponse(error: Exception): BaseErrorResponse? {
        try {
            if (error is HttpException) {
                return gson.fromJson(
                    error.response()?.errorBody()?.string(),
                    BaseErrorResponse::class.java
                )
            }
        } catch (e: Exception) {

        }
        return null
    }

    protected inline fun <reified T> getGenericErrorResponseInfo(errorResponse: BaseErrorResponse?): T? {
        try {
            return gson.fromJson(
                gson.toJson(errorResponse?.info),
                T::class.java
            )
        } catch (e: Exception) {

        }
        return null
    }

    open fun transform(error: Exception): BaseErrorModel {
        if (errorResponse == null) {
            errorResponse = getErrorResponse(error)
        }

        val errorResponseInfo = getGenericErrorResponseInfo<BaseErrorResponseInfo>(errorResponse)
        return when {
            error is HttpException -> {
                return when {
                    error.code() == HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        BaseErrorModel()
                    }
                    errorResponse != null && errorResponseInfo != null -> {
                        when (errorResponseInfo.alertType) {
                            BaseErrorResponseInfo.AlertType.DIALOG -> DialogErrorModel(
                                title = errorResponseInfo.title ?: "",
                                message = errorResponseInfo.message ?: "",
                                positive = "context.getString(R.string.dialog_action_ok_i_see_small)"
                            )
                            BaseErrorResponseInfo.AlertType.TOAST -> ToastErrorModel(
                                message = errorResponseInfo.message ?: ""
                            )
                            else -> UnExpectedErrorModel("context.getString(R.string.error_popup_session_unexpected)")
                        }
                    }

                    else -> UnExpectedErrorModel("context.getString(R.string.error_popup_session_unexpected)")
                }
            }

            error.isNetworkError -> {
                ToastErrorModel("context.getString(R.string.food_common_please_check_you_internet)")
            }

            error is NullPointerException -> {
                NPEErrorModel()
            }

            error is CancellationException -> {
                IgnoreErrorModel()
            }

            else -> {
                UnExpectedErrorModel("context.getString(R.string.error_popup_session_unexpected)")
            }
        }
    }

    protected val Throwable?.isNetworkError: Boolean
        get() = this is NoConnectivityException || this is ConnectException
}
