package com.moneyfoward.githubclone.user.data.networking

import com.moneyfoward.githubclone.core.data.networking.buildUrl
import com.moneyfoward.githubclone.core.data.networking.safeCall
import com.moneyfoward.githubclone.core.domain.NetworkError
import com.moneyfoward.githubclone.core.domain.Result
import com.moneyfoward.githubclone.core.domain.map
import com.moneyfoward.githubclone.user.data.mappers.toUser
import com.moneyfoward.githubclone.user.data.networking.dto.UserDto
import com.moneyfoward.githubclone.user.domain.User
import com.moneyfoward.githubclone.user.domain.UserDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.parameters


class RemoteUserDataSource(
    private val httpClient: HttpClient
): UserDataSource {
    override suspend fun getUsers(page: Int): Result<List<User>, NetworkError> {
        return safeCall<List<UserDto>> {
            httpClient.get(
                urlString = buildUrl("/users")
            ) {
                parameters {
                    append("since", (page*30).toString())
                }
            }
        }.map {
            it.map {
                it.toUser()
            }
        }
    }

    override suspend fun getSearchUsers(query: String): Result<List<User>, NetworkError> {
        return safeCall<List<UserDto>> {
            httpClient.get(
                urlString = buildUrl("/search/users")
            ) {
                parameters {
                    append("q", query)
                    append("page", 1.toString())
                }
            }
        }.map {
            it.map {
                it.toUser()
            }
        }
    }

    override suspend fun getUser(userId: String): Result<User, NetworkError> {
        TODO("Not yet implemented")
    }

}