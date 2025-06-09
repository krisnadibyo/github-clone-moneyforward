package com.moneyfoward.githubclone.core.presentation

import android.content.Context
import com.moneyfoward.githubclone.R
import com.moneyfoward.githubclone.core.domain.NetworkError

fun NetworkError.toString(context: Context): String {
    val resId =
        when (this) {
            NetworkError.REQUEST_TIMEOUT -> R.string.error_request_timeout
            NetworkError.TOO_MANY_REQUEST -> R.string.error_too_many_request
            NetworkError.NO_INTERNET -> R.string.error_no_internet
            NetworkError.SERVER_ERROR -> R.string.error_server_error
            NetworkError.SERIALIZATION -> R.string.error_serialization
            NetworkError.RATE_LIMIT -> R.string.error_rate_limit
            NetworkError.UNKNOWN -> R.string.unknown
        }
    return context.getString(resId)
}
