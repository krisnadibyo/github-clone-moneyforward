package com.moneyfoward.githubclone.core.data.networking

import com.moneyfoward.githubclone.core.data.networking.ConfigApi.API_KEY
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.nio.channels.UnresolvedAddressException

object HttpClientFactory {
    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.ANDROID
            }
            install(HttpTimeout) {
                connectTimeoutMillis = 3000
                requestTimeoutMillis = 7000
                socketTimeoutMillis = 10000
            }

            // Add retry logic
            install(HttpRequestRetry) {
                maxRetries = 3
                retryOnExceptionIf { _, cause ->
                    cause is ConnectTimeoutException || cause is UnresolvedAddressException
                }
                delayMillis { retry -> retry * 1000L }
            }

            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            defaultRequest {
                bearerAuth(API_KEY)
                header("accept","application/vnd.github+json")
            }
        }
    }

}