package com.example.androidassestmentproject.domain.usecase

import com.example.androidassestmentproject.domain.model.ErrorType
import com.example.androidassestmentproject.domain.model.Resource
import com.example.androidassestmentproject.domain.model.User
import com.example.androidassestmentproject.domain.repository.UserRepository
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(query: String): Resource<List<User>> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return Resource.Error(ErrorType.INVALID_QUERY)
        return repository.searchUsers(trimmed)
    }
}
