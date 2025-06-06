package com.moneyfoward.githubclone.user.data.mappers

import com.moneyfoward.githubclone.user.data.networking.dto.UserDto
import com.moneyfoward.githubclone.user.data.networking.dto.UserRepoDto
import com.moneyfoward.githubclone.user.domain.User
import com.moneyfoward.githubclone.user.domain.UserRepo
import kotlin.Long


fun UserDto.toUser(): User {
    return User(
        id = id,
        username = login,
        avatar = avatarUrl
    )
}

fun UserRepoDto.toUserRepo(): UserRepo {
    return UserRepo(
        id = id,
        name = name,
        fullName = fullName,
        isPrivate = isPrivate,
        description = description,
        isFork = isFork,
        htmlUrl = htmlUrl,
        stars = stars,
        languageLink = languageLink,
    )
}