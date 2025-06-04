package com.moneyfoward.githubclone.user.data.mappers

import com.moneyfoward.githubclone.user.data.networking.dto.UserDto
import com.moneyfoward.githubclone.user.domain.User


fun UserDto.toUser(): User {
    return User(
        id = id,
        username = login,
        avatar = avatarUrl
    )
}