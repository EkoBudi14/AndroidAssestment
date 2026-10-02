package com.example.androidassestmentproject.presentation.common

import androidx.annotation.StringRes
import com.example.androidassestmentproject.R
import com.example.androidassestmentproject.domain.model.ErrorType

@StringRes
fun ErrorType.toMessageRes(): Int = when (this) {
    ErrorType.NO_CONNECTION -> R.string.error_no_connection
    ErrorType.RATE_LIMITED -> R.string.error_rate_limited
    ErrorType.NOT_FOUND -> R.string.error_not_found
    ErrorType.INVALID_QUERY -> R.string.error_invalid_query
    ErrorType.UNKNOWN -> R.string.error_unknown
}