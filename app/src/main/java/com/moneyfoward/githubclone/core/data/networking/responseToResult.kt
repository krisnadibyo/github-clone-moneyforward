package com.moneyfoward.githubclone.core.data.networking

import com.moneyfoward.githubclone.core.domain.NetworkError
import com.moneyfoward.githubclone.core.domain.Result
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, NetworkError> =
    when (response.status.value) {
        in 200..200 -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                Result.Error(NetworkError.SERIALIZATION)
            }
        }
        408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
        429 -> Result.Error(NetworkError.TOO_MANY_REQUEST)
        403 -> Result.Error(NetworkError.RATE_LIMIT)
        in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
        else -> Result.Error(NetworkError.UNKNOWN)
    }
