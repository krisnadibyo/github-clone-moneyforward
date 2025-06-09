package com.moneyfoward.githubclone.core.data.networking

import com.moneyfoward.githubclone.BuildConfig

fun buildUrl(url: String): String =
    when {
        url.contains(BuildConfig.BASE_URL) -> url
        url.startsWith("/") -> BuildConfig.BASE_URL + url.drop(1)
        else -> BuildConfig.BASE_URL + url
    }
