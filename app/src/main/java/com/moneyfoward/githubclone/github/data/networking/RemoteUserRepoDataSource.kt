package com.moneyfoward.githubclone.github.data.networking

import com.moneyfoward.githubclone.core.data.networking.ConfigApi.PAGE_SIZE
import com.moneyfoward.githubclone.core.data.networking.buildUrl
import com.moneyfoward.githubclone.core.data.networking.safeCall
import com.moneyfoward.githubclone.core.domain.NetworkError
import com.moneyfoward.githubclone.core.domain.Result
import com.moneyfoward.githubclone.core.domain.map
import com.moneyfoward.githubclone.github.data.mappers.toUserRepo
import com.moneyfoward.githubclone.github.data.networking.dto.UserRepoResponse
import com.moneyfoward.githubclone.github.domain.RepoDataSource
import com.moneyfoward.githubclone.github.domain.model.UserRepo
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.serialization.json.JsonObject


class RemoteUserRepoDataSource(
    private val httpClient: HttpClient
): RepoDataSource {
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

    override suspend fun getLanguageRepo(repoName: String): Result<String, NetworkError> {
        return safeCall<JsonObject> {
            httpClient.get(
                urlString = buildUrl("/repos/${repoName}/languages")
            )
        }.map {
            it.keys.firstOrNull() ?: "Unknown"
        }
    }

}