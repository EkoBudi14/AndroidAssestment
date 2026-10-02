package com.example.androidassestmentproject.domain.usecase

import com.example.androidassestmentproject.domain.model.Resource
import com.example.androidassestmentproject.domain.model.UserDetail
import com.example.androidassestmentproject.domain.repository.UserRepository
import javax.inject.Inject

class GetUserDetailUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(username: String): Resource<UserDetail> =
        repository.getUserDetail(username)
}
