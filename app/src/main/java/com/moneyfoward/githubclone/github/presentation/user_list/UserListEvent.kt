package com.moneyfoward.githubclone.github.presentation.user_list

import com.moneyfoward.githubclone.core.domain.NetworkError


sealed interface UserListEvent {
    data class Error(val error: NetworkError): UserListEvent
}