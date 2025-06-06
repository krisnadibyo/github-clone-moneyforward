package com.moneyfoward.githubclone.github.presentation.user_detail

import com.moneyfoward.githubclone.github.domain.model.User
import com.moneyfoward.githubclone.github.domain.model.UserRepo


data class UserDetailState(
    val user: User = User(id=-1, username = "", avatar = ""),
    val repositories: List<UserRepo> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false
)