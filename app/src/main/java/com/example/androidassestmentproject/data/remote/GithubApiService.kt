package com.example.androidassestmentproject.data.remote

import com.example.androidassestmentproject.data.remote.dto.SearchUserResponse
import com.example.androidassestmentproject.data.remote.dto.UserDetailDto
import com.example.androidassestmentproject.data.remote.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("per_page") perPage: Int = 50
    ): List<UserDto>

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 50
    ): SearchUserResponse

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String
    ): UserDetailDto
}