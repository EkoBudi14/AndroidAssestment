package com.example.androidassestmentproject.presentation.search

import com.example.androidassestmentproject.domain.model.ErrorType
import com.example.androidassestmentproject.domain.model.User

sealed class SearchUiState {
    data object Idle : SearchUiState()
    data object Loading : SearchUiState()
    data object Empty : SearchUiState()
    data class Success(val users: List<User>, val fromCache: Boolean) : SearchUiState()
    data class Error(val errorType: ErrorType) : SearchUiState()
}