package com.moneyfoward.githubclone.core.domain



enum class NetworkError: Error {
    REQUEST_TIMEOUT,
    TOO_MANY_REQUEST,
    RATE_LIMIT,
    NO_INTERNET,
    SERVER_ERROR,
    SERIALIZATION,
    UNKNOWN
}