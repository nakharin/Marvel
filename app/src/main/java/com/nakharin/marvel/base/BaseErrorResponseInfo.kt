package com.nakharin.marvel.base

import com.google.gson.annotations.SerializedName

data class BaseErrorResponseInfo(
    @SerializedName("title") val title: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("alertType") val alertType: AlertType?
) {
    enum class AlertType {
        @SerializedName("DIALOG")
        DIALOG,
        @SerializedName("TOAST")
        TOAST
    }
}
