package com.moneyfoward.githubclone.user.presentation.user_list

import com.moneyfoward.githubclone.user.domain.User

sealed interface UserListAction {
    class OnUserClick(val user: User): UserListAction

    class OnUserSearch(val query: String): UserListAction

    data object OnRefresh: UserListAction

    data object OnScrollToBottom: UserListAction
}