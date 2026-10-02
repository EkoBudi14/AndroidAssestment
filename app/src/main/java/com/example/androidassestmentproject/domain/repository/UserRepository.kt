package com.example.androidassestmentproject.domain.repository

import com.example.androidassestmentproject.domain.model.Resource
import com.example.androidassestmentproject.domain.model.User
import com.example.androidassestmentproject.domain.model.UserDetail
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers(): Resource<List<User>>
    suspend fun searchUsers(query: String): Resource<List<User>>
    fun getUserDetail(username: String): Flow<Resource<UserDetail>>
}