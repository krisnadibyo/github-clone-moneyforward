package com.moneyfoward.githubclone.github.data.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserRepoDto(
    val id: Long,
    val name: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("private")
    val isPrivate: Boolean,
    val description: String?,
    @SerialName("fork")
    val isFork: Boolean,
    @SerialName("html_url")
    val htmlUrl: String,
    @SerialName("stargazers_count")
    val stars: Int,
    @SerialName("languages_url")
    val languageLink: String
)

typealias UserRepoResponse = List<UserRepoDto>