package com.moneyfoward.githubclone.github.domain

import com.moneyfoward.githubclone.core.domain.NetworkError
import com.moneyfoward.githubclone.core.domain.Result
import com.moneyfoward.githubclone.github.domain.model.User
import com.moneyfoward.githubclone.github.domain.model.UserRepo

interface GithubDataSource {

    suspend fun getUsers(nextId: Int): com.moneyfoward.githubclone.core.domain.Result<List<User>, NetworkError>
    suspend fun getSearchUsers(query: String, page: Int): com.moneyfoward.githubclone.core.domain.Result<List<User>, NetworkError>
    suspend fun getUser(userId: String): Result<User, NetworkError>
    suspend fun getUserRepos(username: String, page: Int): Result<List<UserRepo>, NetworkError>

}