package com.example.androidassestmentproject.domain.repository

import com.example.androidassestmentproject.domain.model.Resource
import com.example.androidassestmentproject.domain.model.User
import com.example.androidassestmentproject.domain.model.UserDetail

interface UserRepository {
    suspend fun getUsers(): Resource<List<User>>
    suspend fun searchUsers(query: String): Resource<List<User>>
    suspend fun getUserDetail(username: String): Resource<UserDetail>
}