package com.moneyfoward.githubclone.github.presentation.user_detail

import com.moneyfoward.githubclone.core.domain.NetworkError

sealed interface UserDetailEvent {
    data class Error(val error: NetworkError): UserDetailEvent

}