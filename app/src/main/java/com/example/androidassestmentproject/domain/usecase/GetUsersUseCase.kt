package com.example.androidassestmentproject.domain.usecase

import com.example.androidassestmentproject.domain.model.Resource
import com.example.androidassestmentproject.domain.model.User
import com.example.androidassestmentproject.domain.repository.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Resource<List<User>> = repository.getUsers()
}