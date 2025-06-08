package com.moneyfoward.githubclone.github.domain.model

import kotlin.math.floor

data class UserRepo(
    val id: Long,
    val name: String,
    val fullName: String,
    val isPrivate: Boolean? = null,
    val description: String = "",
    val isFork: Boolean,
    val htmlUrl: String = "",
    val stars: Int = 0,
    val language: String = "Unknown"
)

fun UserRepo.stars(): String {
    if (stars < 1000) {
        return stars.toString()
    } else {
        val shorten = (stars / 1000f)
        val roundedDown = floor(shorten * 10) / 10
        val formatted = String.format("%.1f", roundedDown)
        return "${formatted}K"
    }
}