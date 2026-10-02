package com.example.androidassestmentproject.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidassestmentproject.domain.model.Resource
import com.example.androidassestmentproject.domain.usecase.GetUserDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserDetailUseCase: GetUserDetailUseCase
) : ViewModel() {

    private val username: String = checkNotNull(savedStateHandle.get<String>(KEY_USERNAME)) {
        "Username is required to open user detail"
    }
    private var loadJob: Job? = null

    private val _uiState = MutableLiveData<UserDetailUiState>()
    val uiState: LiveData<UserDetailUiState> = _uiState

    init {
        loadUserDetail()
    }

    fun loadUserDetail() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = UserDetailUiState.Loading
            getUserDetailUseCase(username).collect { result ->
                _uiState.value = when (result) {
                    is Resource.Success -> UserDetailUiState.Success(result.data, result.fromCache)
                    is Resource.Error -> UserDetailUiState.Error(result.errorType)
                }
            }
        }
    }

    companion object {
        const val KEY_USERNAME = "extra_username"
    }
}