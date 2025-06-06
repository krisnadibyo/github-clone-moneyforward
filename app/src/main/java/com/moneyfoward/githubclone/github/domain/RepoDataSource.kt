package com.moneyfoward.githubclone.github.domain

import com.moneyfoward.githubclone.core.domain.NetworkError
import com.moneyfoward.githubclone.core.domain.Result
import com.moneyfoward.githubclone.github.domain.model.UserRepo

interface RepoDataSource {
    suspend fun getUserRepos(username: String, page: Int): Result<List<UserRepo>, NetworkError>
    suspend fun getLanguageRepo(repoName: String): Result<String, NetworkError>
}