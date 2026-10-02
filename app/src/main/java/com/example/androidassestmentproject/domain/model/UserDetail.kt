package com.example.androidassestmentproject.domain.model

data class UserDetail(
    val id: Long,
    val username: String,
    val name: String?,
    val avatarUrl: String,
    val bio: String?,
    val company: String?,
    val location: String?,
    val blog: String?,
    val publicRepos: Int,
    val followers: Int,
    val following: Int
)