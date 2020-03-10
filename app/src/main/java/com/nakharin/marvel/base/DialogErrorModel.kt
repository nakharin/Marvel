package com.nakharin.marvel.base

data class DialogErrorModel(
    val title: String,
    val message: String,
    val positive: String
) : BaseErrorModel()
