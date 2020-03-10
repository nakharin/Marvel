package com.nakharin.marvel.data.source

import com.nakharin.marvel.data.api.ResponseWrapper
import com.nakharin.marvel.data.api.Code
import com.nakharin.marvel.utils.extension.convertToJsonModel
import com.nakharin.marvel.presentation.content.model.ContentResponse

class ContentLocalDataSource {

    companion object {
        private const val sourceStr: String = "{\"title\":\"Civil War\",\"image\":[\"http://movie.phinf.naver.net/20151127_272/1448585271749MCMVs_JPEG/movie_image.jpg?type=m665_443_2\",\"http://movie.phinf.naver.net/20151127_84/1448585272016tiBsF_JPEG/movie_image.jpg?type=m665_443_2\",\"http://movie.phinf.naver.net/20151125_36/1448434523214fPmj0_JPEG/movie_image.jpg?type=m665_443_2\"]}"
    }

    fun generate(): ResponseWrapper<ContentResponse> {
        return ResponseWrapper<ContentResponse>().apply {
            success = true
            code = Code.SUCCEEDED
            message = "Success"
            data = sourceStr.convertToJsonModel<ContentResponse>()
        }
    }
}
