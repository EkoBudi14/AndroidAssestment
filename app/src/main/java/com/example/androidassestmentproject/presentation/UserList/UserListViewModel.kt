package com.example.androidassestmentproject.presentation.UserList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidassestmentproject.domain.model.Resource
import com.example.androidassestmentproject.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<UserListUiState>()
    val uiState: LiveData<UserListUiState> = _uiState

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = UserListUiState.Loading
            _uiState.value = when (val result = getUsersUseCase()) {
                is Resource.Success -> UserListUiState.Success(result.data, result.fromCache)
                is Resource.Error -> UserListUiState.Error(result.errorType)
            }
        }
    }
}