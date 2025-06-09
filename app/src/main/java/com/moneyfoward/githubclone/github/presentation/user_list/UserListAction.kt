package com.moneyfoward.githubclone.github.presentation.user_list

sealed interface UserListAction {
    class OnUserSearch(
        val query: String,
    ) : UserListAction

    data object OnRefresh : UserListAction

    data object OnScrollToBottom : UserListAction
}
