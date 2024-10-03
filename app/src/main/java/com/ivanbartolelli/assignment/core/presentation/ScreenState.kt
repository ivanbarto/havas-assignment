package com.ivanbartolelli.assignment.core.presentation

sealed class ScreenState<out T> {
    object Loading : ScreenState<Nothing>()
    data class Success<out T>(val data: T) : ScreenState<T>()
    data class Error(val error: ErrorType) : ScreenState<Nothing>()
}