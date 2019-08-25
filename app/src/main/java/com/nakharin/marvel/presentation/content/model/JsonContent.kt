package com.nakharin.marvel.presentation.content.model

import android.os.Parcel
import com.google.gson.annotations.SerializedName
import com.nakharin.marvel.extension.KParcelable
import com.nakharin.marvel.extension.parcelableCreator

class JsonContent() : KParcelable {

    @SerializedName("title")
    var title: String = ""

    @SerializedName("image")
    var images: ArrayList<String> = arrayListOf()

    constructor(parcel: Parcel) : this() {
        title = parcel.readString() ?: ""
        images = parcel.createStringArrayList() ?: arrayListOf()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeStringList(images)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::JsonContent)
    }
}