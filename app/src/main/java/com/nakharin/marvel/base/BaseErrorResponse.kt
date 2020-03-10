package com.nakharin.marvel.base

import com.google.gson.annotations.SerializedName

data class BaseErrorResponse(
    @SerializedName("code") val code: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("info") var info: Any?,
    @SerializedName("timestamp") val timestamp: String?
)
