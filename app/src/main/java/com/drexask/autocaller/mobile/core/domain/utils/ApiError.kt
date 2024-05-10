package com.drexask.autocaller.mobile.core.domain.utils

sealed interface ApiError: Error {
    sealed interface TokenStatusError: ApiError {
        sealed interface Remote: TokenStatusError {
            data class UnknownError(val text: String): Remote
        }
    }
    sealed interface CallTasksError: ApiError {
        sealed interface Remote: CallTasksError {
            data class UnknownError(val text: String): Remote
        }
    }
    enum class ServerConnectionSettingsError: ApiError {
        FILE_DOES_NOT_EXIST
    }
    enum class Network: ApiError {
        CONNECTION_REFUSED,
        REQUEST_TIMEOUT
    }
    data class UnknownError(val text: String) : ApiError
}