package com.moneyfoward.githubclone.core.data.networking

import com.moneyfoward.githubclone.core.domain.NetworkError
import com.moneyfoward.githubclone.core.domain.Result
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T, NetworkError> {
    val response =
        try {
            execute()
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        } catch (e: HttpRequestTimeoutException) {
            return Result.Error(NetworkError.REQUEST_TIMEOUT)
        } catch (e: ConnectTimeoutException) {
            return Result.Error(NetworkError.REQUEST_TIMEOUT)
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            return Result.Error(NetworkError.UNKNOWN)
        }
    return responseToResult(response)
}
