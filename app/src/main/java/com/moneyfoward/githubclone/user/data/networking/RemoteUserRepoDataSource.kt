package com.moneyfoward.githubclone.user.data.networking

import com.moneyfoward.githubclone.core.data.networking.ConfigApi
import com.moneyfoward.githubclone.core.data.networking.buildUrl
import com.moneyfoward.githubclone.core.data.networking.safeCall
import com.moneyfoward.githubclone.core.domain.NetworkError
import com.moneyfoward.githubclone.core.domain.Result
import com.moneyfoward.githubclone.core.domain.map
import com.moneyfoward.githubclone.user.data.mappers.toUserRepo
import com.moneyfoward.githubclone.user.data.networking.dto.UserRepoDto
import com.moneyfoward.githubclone.user.domain.RepoDataSource
import com.moneyfoward.githubclone.user.domain.User
import com.moneyfoward.githubclone.user.domain.UserRepo
import io.ktor.client.HttpClient
import io.ktor.client.request.get


class RemoteUserRepoDataSource(
    private val httpClient: HttpClient
): RepoDataSource {
    override suspend fun getUserRepos(username: String, page: Int): Result<List<UserRepo>, NetworkError> {
        return safeCall<List<UserRepoDto>> {
            httpClient.get(
                urlString = buildUrl("/users/${username}/repos")
            ) {
                url {
                    it.parameters.append("page",page.toString())
                    it.parameters.append("per_page", ConfigApi.PAGE_SIZE.toString())
                    it.parameters.append("sort","updated")
                }
            }
        }.map {
            it.map {
                it.toUserRepo()
            }
        }
    }

    override suspend fun getLanguageRepo(repoName: String): Result<String, NetworkError> {
        return safeCall<Map<String, Any>> {
            httpClient.get(
                urlString = buildUrl("/repos/${repoName}/languages")
            )
        }.map {
            it.keys.first()
        }
    }

}