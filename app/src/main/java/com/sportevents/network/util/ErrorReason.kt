package com.sportevents.network.util

sealed class ErrorReason {
    object NetworkConnection : ErrorReason()
    object Authorization : ErrorReason()
    data class Unspecified(val message: String?) : ErrorReason()
}
