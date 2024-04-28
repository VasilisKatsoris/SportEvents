package com.sportevents.network.util

import retrofit2.Response

sealed class ApiResult<out T> {
    data class Ok<out T>(val data: T) : ApiResult<T>()
    data object Error : ApiResult<Nothing>()
}

/**
 * Executes the given [apiCallFun], catches errors and converts the result into an [ApiResult].
 */
suspend fun <T> apiCall(apiCallFun: suspend () -> Response<T>): ApiResult<T> = try {
    val response = apiCallFun()
    if (response.isSuccessful) {
        response.body()?.let { ApiResult.Ok(it) } ?: ApiResult.Error
    } else {
        ApiResult.Error
    }
} catch (e: Exception) {
    e.printStackTrace()
    ApiResult.Error
}
