package com.moneyfoward.githubclone.github.data.mappers

import com.moneyfoward.githubclone.github.data.networking.dto.UserDto
import com.moneyfoward.githubclone.github.data.networking.dto.UserRepoDto
import com.moneyfoward.githubclone.github.domain.model.User
import com.moneyfoward.githubclone.github.domain.model.UserRepo


fun UserDto.toUser(): User {
    return User(
        id = id,
        username = login,
        avatar = avatarUrl ?: "",
        name = name ?: "",
        bio = bio ?: "",
        location = location ?: "",
        followers = followers,
        following = following,
        company = company ?: ""

    )
}

fun UserRepoDto.toUserRepo(): UserRepo {
    return UserRepo(
        id = id,
        name = name,
        fullName = fullName,
        isPrivate = isPrivate,
        description = description ?: "",
        isFork = isFork,
        htmlUrl = htmlUrl,
        stars = stars,
        language = "",
    )
}