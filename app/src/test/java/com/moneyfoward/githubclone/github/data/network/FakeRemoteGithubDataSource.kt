package com.moneyfoward.githubclone.github.data.network

import com.moneyfoward.githubclone.core.domain.NetworkError
import com.moneyfoward.githubclone.core.domain.Result
import com.moneyfoward.githubclone.github.domain.GithubDataSource
import com.moneyfoward.githubclone.github.domain.model.User
import com.moneyfoward.githubclone.github.domain.model.UserRepo
import kotlinx.coroutines.delay


class FakeRemoteGithubDataSource : GithubDataSource {

    private val users = mutableListOf<User>()
    private val userRepo = mutableListOf<UserRepo>()
    private var shouldReturnError = false
    private var errorType: NetworkError = NetworkError.UNKNOWN
    var searchCount = 0
        private set

    fun addUsers(newUsers: List<User>) {
        users.addAll(newUsers)
    }

    fun addUserRepo(newRepos: List<UserRepo>) {
        userRepo.addAll(newRepos)
    }

    fun setShouldReturnError(shouldError: Boolean, error: NetworkError = NetworkError.UNKNOWN) {
        shouldReturnError = shouldError
        errorType = error
    }

    fun clearUser() {
        users.clear()
    }

    fun clearRepo() {
        userRepo.clear()
    }

    fun clearData() {
        users.clear()
        userRepo.clear()
        searchCount = 0
    }


    override suspend fun getUsers(nextId: Int): Result<List<User>, NetworkError> {
        if (shouldReturnError) {
            return Result.Error(errorType)
        }
        return Result.Success(
            users.toList()
        )
    }

    override suspend fun getSearchUsers(
        query: String,
        page: Int
    ): Result<List<User>, NetworkError> {
        if (shouldReturnError) {
            return Result.Error(errorType)
        }
        searchCount += 1
        return Result.Success(
            users.toList()
        )
    }

    override suspend fun getUser(username: String): Result<User, NetworkError> {
        if (shouldReturnError) {
            return Result.Error(errorType)
        }
        return Result.Success(users.get(0).copy())
    }

    override suspend fun getUserRepos(
        username: String,
        page: Int
    ): Result<List<UserRepo>, NetworkError> {
        delay(1000)
        if (shouldReturnError) {
            return Result.Error(errorType)
        }
        return Result.Success(
            userRepo.toList()
        )
    }

}

