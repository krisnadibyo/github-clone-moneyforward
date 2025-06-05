package com.moneyfoward.githubclone.user.data.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSearchResponseDto(
    @SerialName("total_count")
    val itemCount: Int,

    @SerialName("items")
    val items: List<UserDto>
)
