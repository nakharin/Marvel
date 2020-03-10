package com.nakharin.marvel.data.api

import com.google.gson.annotations.SerializedName

data class ResponseWrapper<T>(
    @SerializedName("refCodeApi")
    val refCodeApi: String = "",

    @SerializedName("success")
    var success: Boolean = false,

    @SerializedName("code")
    var code: Code = Code.UNDEFINED,

    @SerializedName("message")
    var message: String = "",

    @SerializedName("errors")
    var errors: ArrayList<String> = arrayListOf(),

    @SerializedName("data")
    var data: T? = null
) {
    enum class Code(var code: Int) {
        @SerializedName("-1")
        UNDEFINED(-1),
        @SerializedName("0")
        SUCCEEDED(0),
        @SerializedName("1")
        FAILED(1)
    }
}

val ResponseWrapper<*>.successfully
    get() = success && data != null

fun <T> ResponseWrapper<T>.isSuccessfully(succeeded: (T) -> Unit, failed: (String) -> Unit) {
    if (success && data != null) {
        succeeded(data!!)
    } else {
        failed(message)
    }
}
