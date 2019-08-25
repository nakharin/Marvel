package com.nakharin.marvel.data.source

import com.nakharin.marvel.data.api.ApiResponse
import com.nakharin.marvel.data.api.Code
import com.nakharin.marvel.extension.convertToJsonModel
import com.nakharin.marvel.presentation.content.model.JsonContent

class ContentDataSource {

    companion object {
        private const val sourceStr: String = "{\"title\":\"Civil War\",\"image\":[\"http://movie.phinf.naver.net/20151127_272/1448585271749MCMVs_JPEG/movie_image.jpg?type=m665_443_2\",\"http://movie.phinf.naver.net/20151127_84/1448585272016tiBsF_JPEG/movie_image.jpg?type=m665_443_2\",\"http://movie.phinf.naver.net/20151125_36/1448434523214fPmj0_JPEG/movie_image.jpg?type=m665_443_2\"]}"
    }

    fun generate(): ApiResponse<JsonContent> {
        return ApiResponse<JsonContent>().apply {
            success = true
            code = Code.SUCCESS
            message = "Success"
            data = sourceStr.convertToJsonModel<JsonContent>()
        }
    }
}