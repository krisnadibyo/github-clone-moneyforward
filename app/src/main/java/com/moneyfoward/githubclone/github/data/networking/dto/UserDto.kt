package com.moneyfoward.githubclone.github.data.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserDto(
    val id: Int,
    @SerialName("login")
    val login: String,
    @SerialName("avatar_url")
    val avatarUrl: String?,
    val name: String? = null,
    val company: String? = null,
    val location: String? = null,
    val bio: String? = null,
    val followers: Int = -1,
    val following: Int = -1
)

typealias UserListResponse = List<UserDto>

typealias UserDetailResponse = UserDto