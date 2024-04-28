package com.sportevents.network.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

sealed interface LoadingEvent<out T> {

    object Loading : LoadingEvent<Nothing>

    data class Success<out T>(val data: T) : LoadingEvent<T>

    @JvmInline
    value class Error(val reason: ErrorReason) : LoadingEvent<Nothing>
}

inline fun <T, R> LoadingEvent<T>.map(transform: (T) -> R): LoadingEvent<R> = when (this) {
    is LoadingEvent.Error -> this
    is LoadingEvent.Loading -> this
    is LoadingEvent.Success -> LoadingEvent.Success(transform(this.data))
}

val <T> Flow<LoadingEvent<T>>.dataValues
    get() = this
        .filterIsInstance<LoadingEvent.Success<T>>()
        .map { it.data }
        .distinctUntilChanged()


fun <T> statefulApiCall(
    apiCallFun: suspend () -> Response<T>
): Flow<LoadingEvent<T>> = flow {
    emit(LoadingEvent.Loading)
    val resultEvent = try {
        val response = apiCallFun()
        val body = response.body()

        if (response.isSuccessful && body != null) {
            LoadingEvent.Success((body))
        } else {
            LoadingEvent.Error(
                when (response.code()) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> ErrorReason.Authorization
                    else -> ErrorReason.Unspecified(response.message())
                }
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
        val error = when (e) {
            is SocketTimeoutException,
            is UnknownHostException,
            is SSLHandshakeException -> ErrorReason.NetworkConnection

            else -> ErrorReason.Unspecified(e.message)
        }
        LoadingEvent.Error(error)
    }
    emit(resultEvent)
}
