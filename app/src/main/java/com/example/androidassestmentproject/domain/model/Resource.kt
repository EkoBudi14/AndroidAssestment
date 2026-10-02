package com.example.androidassestmentproject.domain.model

sealed class Resource<out T> {
    data class Success<T>(val data: T, val fromCache: Boolean = false) : Resource<T>()
    data class Error(val errorType: ErrorType) : Resource<Nothing>()
}