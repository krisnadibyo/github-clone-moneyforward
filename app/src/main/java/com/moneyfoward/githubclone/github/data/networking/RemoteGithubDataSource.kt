package com.moneyfoward.githubclone.github.data.networking

import com.moneyfoward.githubclone.core.data.networking.ConfigApi.PAGE_SIZE
import com.moneyfoward.githubclone.core.data.networking.buildUrl
import com.moneyfoward.githubclone.core.data.networking.safeCall
import com.moneyfoward.githubclone.core.domain.NetworkError
import com.moneyfoward.githubclone.core.domain.Result
import com.moneyfoward.githubclone.core.domain.map
import com.moneyfoward.githubclone.github.data.mappers.toUser
import com.moneyfoward.githubclone.github.data.mappers.toUserRepo
import com.moneyfoward.githubclone.github.data.networking.dto.UserDetailResponse
import com.moneyfoward.githubclone.github.data.networking.dto.UserListResponse
import com.moneyfoward.githubclone.github.data.networking.dto.UserRepoResponse
import com.moneyfoward.githubclone.github.data.networking.dto.UserSearchResponseDto
import com.moneyfoward.githubclone.github.domain.GithubDataSource
import com.moneyfoward.githubclone.github.domain.model.User
import com.moneyfoward.githubclone.github.domain.model.UserRepo
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlin.collections.map


class RemoteGithubDataSource(
    private val httpClient: HttpClient
): GithubDataSource {
    override suspend fun getUsers(since: Int): com.moneyfoward.githubclone.core.domain.Result<List<User>, NetworkError> {
        return safeCall<UserListResponse> {
            httpClient.get(
                urlString = buildUrl("/users")
            ) {
                url {
                    it.parameters.append("since",since.toString())
                    it.parameters.append("per_page", PAGE_SIZE.toString())
                }
            }
        }.map {
            it.map {
                it.toUser()
            }
        }
    }

    override suspend fun getSearchUsers(query: String, page: Int): com.moneyfoward.githubclone.core.domain.Result<List<User>, NetworkError> {
        return safeCall<UserSearchResponseDto> {
            httpClient.get(
                urlString = buildUrl("/search/users")
            ) {
                url {
                    it.parameters.append("q",query)
                    it.parameters.append("page",page.toString())
                    it.parameters.append("per_page", PAGE_SIZE.toString())
                }
            }
        }.map {
            it.items.map {
                it.toUser()
            }
        }

    }

    override suspend fun getUser(username: String): Result<User, NetworkError> {
        return safeCall<UserDetailResponse> {
            httpClient.get(
                urlString = buildUrl("/users/${username}")
            )
        }.map {
            it.toUser()
        }
    }

    override suspend fun getUserRepos(username: String, page: Int): Result<List<UserRepo>, NetworkError> {
        return safeCall<UserRepoResponse> {
            httpClient.get(
                urlString = buildUrl("/users/${username}/repos")
            ) {
                url {
                    it.parameters.append("page",page.toString())
                    it.parameters.append("per_page", PAGE_SIZE.toString())
                    it.parameters.append("sort","updated")
                }
            }
        }.map {
            it.map {
                it.toUserRepo()
            }
        }
    }
}