package com.moneyfoward.githubclone.user.domain

data class User(
    val id: Int,
    val username: String,
    val avatar: String,
    val name: String = "",
    val company: String = "",
    val location: String = "",
    val bio: String = "",
    val followers: Int = -1,
    val following: Int = -1

)