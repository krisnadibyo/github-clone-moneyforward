package com.moneyfoward.githubclone.github.presentation.user_detail

sealed interface UserDetailAction {
    data object OnScrollToBottom : UserDetailAction

    data object OnRefresh : UserDetailAction
}
