package com.moneyfoward.githubclone.github.data.network

import com.moneyfoward.githubclone.core.domain.NetworkError
import com.moneyfoward.githubclone.core.domain.Result
import com.moneyfoward.githubclone.github.domain.GithubDataSource
import com.moneyfoward.githubclone.github.domain.model.User
import com.moneyfoward.githubclone.github.domain.model.UserRepo


class RemoteGithubDataSourceFake : GithubDataSource {
    override suspend fun getUsers(nextId: Int): Result<List<User>, NetworkError> {
        TODO("Not yet implemented")
    }

    override suspend fun getSearchUsers(
        query: String,
        page: Int
    ): Result<List<User>, NetworkError> {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(userId: String): Result<User, NetworkError> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserRepos(
        username: String,
        page: Int
    ): Result<List<UserRepo>, NetworkError> {
        TODO("Not yet implemented")
    }

}