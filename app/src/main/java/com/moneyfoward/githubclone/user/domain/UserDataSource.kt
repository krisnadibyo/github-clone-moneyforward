package com.moneyfoward.githubclone.user.domain

import com.moneyfoward.githubclone.core.domain.NetworkError
import com.moneyfoward.githubclone.core.domain.Result


interface UserDataSource {

    suspend fun getUsers(): Result<List<User>, NetworkError>

    suspend fun getSearchUsers(query: String): Result<List<User>, NetworkError>

    suspend fun getUser(userId: String): Result<User, NetworkError>

}