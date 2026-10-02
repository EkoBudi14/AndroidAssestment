package com.example.androidassestmentproject.presentation.userlist

import com.example.androidassestmentproject.domain.model.ErrorType
import com.example.androidassestmentproject.domain.model.User

sealed class UserListUiState {
    data object Loading : UserListUiState()
    data class Success(val users: List<User>, val fromCache: Boolean) : UserListUiState()
    data class Error(val errorType: ErrorType) : UserListUiState()
}