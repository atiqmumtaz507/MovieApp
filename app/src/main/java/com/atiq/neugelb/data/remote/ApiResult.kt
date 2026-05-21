package com.atiq.neugelb.data.remote

import okio.IOException

sealed class ApiResult<out T> {
    data class Success<T>(val  data: T): ApiResult<T>()
    data class HttpError(val code: Int, val message: String?): ApiResult<Nothing>()
    data class UnknownError(val throwable: Throwable): ApiResult<Nothing>()
    data class NetworkError(val exception: IOException): ApiResult<Nothing>()

}