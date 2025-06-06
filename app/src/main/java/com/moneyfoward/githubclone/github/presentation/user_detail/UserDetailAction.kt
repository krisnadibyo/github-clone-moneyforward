package com.moneyfoward.githubclone.github.presentation.user_detail


sealed interface UserDetailAction {
    class OnClickRepository(val link: String): UserDetailAction
    data object OnScrollToBottom: UserDetailAction

    data object OnRefresh: UserDetailAction

}