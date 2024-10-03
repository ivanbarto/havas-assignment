package com.ivanbartolelli.assignment.core.presentation

import android.content.Context
import com.ivanbartolelli.assignment.R
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toErrorType(): ErrorType {
    return when (this) {
        is UnknownHostException -> ErrorType.NoInternet
        is SocketTimeoutException -> ErrorType.Timeout
        is ConnectException -> ErrorType.ServerUnreachable
        is IOException -> ErrorType.NetworkError
        else -> ErrorType.Unknown
    }
}

enum class ErrorType {
    NoInternet,
    Timeout,
    ServerUnreachable,
    NetworkError,
    Unknown
}

fun ErrorType.text(context: Context): String = when (this) {
    ErrorType.NoInternet -> context.getString(R.string.text_error_no_internet)
    ErrorType.Timeout -> context.getString(R.string.text_error_timeout)
    ErrorType.ServerUnreachable -> context.getString(R.string.text_error_server_unreachable)
    ErrorType.NetworkError -> context.getString(R.string.text_error_network)
    ErrorType.Unknown -> context.getString(R.string.text_error_unknown)
}