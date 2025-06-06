package com.moneyfoward.githubclone.ui.nav


sealed class ScreenRoute(val route: String) {
    object UserList: ScreenRoute("user")
    object UserDetail: ScreenRoute("user/{username}")
    object RepositoryDetail: ScreenRoute("repository/{username}/{repo}")
}