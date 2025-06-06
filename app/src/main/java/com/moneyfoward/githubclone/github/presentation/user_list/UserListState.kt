package com.moneyfoward.githubclone.github.presentation.user_list

import com.moneyfoward.githubclone.github.domain.model.User


data class UserListState(
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val users: List<User> = emptyList(),
    val selectedUser: User? = null,
    val query: String? = null,
)