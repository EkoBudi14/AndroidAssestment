package com.example.androidassestmentproject.domain.usecase

import com.example.androidassestmentproject.domain.model.Resource
import com.example.androidassestmentproject.domain.model.UserDetail
import com.example.androidassestmentproject.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDetailUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(username: String): Flow<Resource<UserDetail>> =
        repository.getUserDetail(username)
}
