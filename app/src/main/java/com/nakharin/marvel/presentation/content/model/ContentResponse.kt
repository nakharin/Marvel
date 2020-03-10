package com.nakharin.marvel.presentation.content.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContentResponse(
    @SerializedName("title")
    var title: String = "",

    @SerializedName("image")
    var images: ArrayList<String> = arrayListOf()
) : Parcelable
