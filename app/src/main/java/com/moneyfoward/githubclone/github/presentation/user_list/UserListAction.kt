package com.moneyfoward.githubclone.github.presentation.user_list

import com.moneyfoward.githubclone.github.domain.model.User

sealed interface UserListAction {
    class OnUserClick(val user: User): UserListAction

    class OnUserSearch(val query: String): UserListAction

    data object OnRefresh: UserListAction

    data object OnScrollToBottom: UserListAction
}