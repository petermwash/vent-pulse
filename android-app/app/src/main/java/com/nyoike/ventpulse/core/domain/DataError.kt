package com.nyoike.ventpulse.core.domain

sealed interface DataError : Error {
    enum class Local : DataError {
        NOT_FOUND,
        UNKNOWN
    }

    enum class Network : DataError {
        UNAUTHORIZED,
        NO_INTERNET,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }
}
