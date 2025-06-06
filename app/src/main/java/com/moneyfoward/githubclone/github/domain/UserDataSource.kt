package com.moneyfoward.githubclone.github.domain

import com.moneyfoward.githubclone.core.domain.NetworkError
import com.moneyfoward.githubclone.core.domain.Result
import com.moneyfoward.githubclone.github.domain.model.User


interface UserDataSource {

    suspend fun getUsers(nextId: Int): Result<List<User>, NetworkError>

    suspend fun getSearchUsers(query: String, page: Int): Result<List<User>, NetworkError>

    suspend fun getUser(userId: String): Result<User, NetworkError>

}