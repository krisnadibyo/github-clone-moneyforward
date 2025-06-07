package com.moneyfoward.githubclone.github.domain.model

import android.annotation.SuppressLint
import android.graphics.Color

data class UserRepo(
    val id: Long,
    val name: String,
    val fullName: String,
    val isPrivate: Boolean,
    val description: String,
    val isFork: Boolean,
    val htmlUrl: String,
    val stars: Int,
    val language: String
)

fun UserRepo.stars(): String {
    if (stars < 1000) {
        return stars.toString()
    } else {
        val shorten = stars / 1000f
        val formatted = String.format("%.1f", shorten)
        return "${formatted}K"
    }
}