package com.moneyfoward.githubclone.user.domain

import android.annotation.SuppressLint

data class UserRepo(
    val id: Long,
    val name: String,
    val fullName: String,
    val isPrivate: Boolean,
    val description: String,
    val isFork: Boolean,
    val htmlUrl: String,
    val stars: Int,
    val languageLink: String
)

val dummyUserRepo = UserRepo(
    id = 12,
    name = "peerless",
    fullName = "krisna/peerless",
    isPrivate = false,
    description = "Peerless is standalone application which runs games on macos",
    isFork = false,
    htmlUrl = "",
    stars = 132312,
    languageLink = "Ruby"
)

@SuppressLint("DefaultLocale")
fun UserRepo.stars(): String {
    if (stars < 1000) {
        return stars.toString()
    } else {
        val shorten = stars / 1000f
        val formatted = String.format("%.1f", shorten)
        return "${formatted}K"
    }
}