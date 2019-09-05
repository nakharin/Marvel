package com.nakharin.marvel.utils.extension

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> String.convertToJsonModel(): T {
    return Gson().fromJson(this, T::class.java)
}

inline fun <reified T> Any.convertToJsonModel(): T {
    return Gson().fromJson(Gson().toJson(this), T::class.java)
}

inline fun <reified T> genericType() = object: TypeToken<T>() {}.type!!