package com.moneyfoward.githubclone.user.data.networking

import com.moneyfoward.githubclone.core.data.networking.ConfigApi
import com.moneyfoward.githubclone.core.data.networking.buildUrl
import com.moneyfoward.githubclone.core.data.networking.safeCall
import com.moneyfoward.githubclone.core.domain.NetworkError
import com.moneyfoward.githubclone.core.domain.Result
import com.moneyfoward.githubclone.core.domain.map
import com.moneyfoward.githubclone.user.data.mappers.toUser
import com.moneyfoward.githubclone.user.data.networking.dto.UserDto
import com.moneyfoward.githubclone.user.data.networking.dto.UserSearchResponseDto
import com.moneyfoward.githubclone.user.domain.User
import com.moneyfoward.githubclone.user.domain.UserDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import io.ktor.http.parameters


class RemoteUserDataSource(
    private val httpClient: HttpClient
): UserDataSource {
    override suspend fun getUsers(since: Int): Result<List<User>, NetworkError> {
        return safeCall<List<UserDto>> {
            httpClient.get(
                urlString = buildUrl("/users")
            ) {
               url {
                   it.parameters.append("since",since.toString())
                   it.parameters.append("per_page", ConfigApi.PAGE_SIZE.toString())
               }
            }
        }.map {
            it.map {
                it.toUser()
            }
        }
    }

    override suspend fun getSearchUsers(query: String, page: Int): Result<Pair<Int, List<User>>, NetworkError> {
        return safeCall<UserSearchResponseDto> {
            httpClient.get(
                urlString = buildUrl("/search/users")
            ) {
                url {
                    it.parameters.append("q",query)
                    it.parameters.append("page",page.toString())
                    it.parameters.append("per_page", ConfigApi.PAGE_SIZE.toString())
                }
            }
        }.map {
            Pair( it.itemCount, it.items.map {
                it.toUser()
            })

        }

    }

    override suspend fun getUser(userId: String): Result<User, NetworkError> {
        TODO("Not yet implemented")
    }

}