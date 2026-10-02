package com.example.androidassestmentproject.presentation.detail


import com.example.androidassestmentproject.domain.model.ErrorType
import com.example.androidassestmentproject.domain.model.UserDetail

sealed class UserDetailUiState {
    data object Loading : UserDetailUiState()
    data class Success(val userDetail: UserDetail, val fromCache: Boolean) : UserDetailUiState()
    data class Error(val errorType: ErrorType) : UserDetailUiState()
}