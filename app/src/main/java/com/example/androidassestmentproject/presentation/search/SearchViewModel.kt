package com.example.androidassestmentproject.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidassestmentproject.domain.model.Resource
import com.example.androidassestmentproject.domain.network.NetworkMonitor
import com.example.androidassestmentproject.domain.usecase.SearchUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUsersUseCase: SearchUsersUseCase,
    networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableLiveData<SearchUiState>(SearchUiState.Idle)
    val uiState: LiveData<SearchUiState> = _uiState

    val isOnline: LiveData<Boolean> = networkMonitor.isOnline.asLiveData()

    private var searchJob: Job? = null
    private var lastQuery = ""

    fun search(query: String) {
        val keyword = query.trim()
        if (keyword.isEmpty()) return

        lastQuery = keyword
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            _uiState.value = when (val result = searchUsersUseCase(keyword)) {
                is Resource.Success -> {
                    if (result.data.isEmpty()) SearchUiState.Empty
                    else SearchUiState.Success(result.data, result.fromCache)
                }

                is Resource.Error -> SearchUiState.Error(result.errorType)
            }
        }
    }

    fun retry() {
        search(lastQuery)
    }
}