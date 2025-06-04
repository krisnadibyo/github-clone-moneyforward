package com.moneyfoward.githubclone.user.presentation.user_list

import com.moneyfoward.githubclone.user.domain.User

sealed interface UserListAction {
    class onUserClick(val user: User): UserListAction

    data object onRefresh: UserListAction

    data object onScrollToBottom: UserListAction
}