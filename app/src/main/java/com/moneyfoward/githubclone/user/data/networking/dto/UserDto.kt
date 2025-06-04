package com.moneyfoward.githubclone.user.data.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserDto(
    val id: Int,
    @SerialName("login")
    val login: String,
    @SerialName("avatar_url")
    val avatarUrl: String
)