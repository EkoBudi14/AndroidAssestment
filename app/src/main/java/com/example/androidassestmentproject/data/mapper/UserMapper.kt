package com.example.androidassestmentproject.data.mapper

import com.example.androidassestmentproject.data.local.entity.UserDetailEntity
import com.example.androidassestmentproject.data.local.entity.UserEntity
import com.example.androidassestmentproject.data.remote.dto.UserDetailDto
import com.example.androidassestmentproject.data.remote.dto.UserDto
import com.example.androidassestmentproject.domain.model.User
import com.example.androidassestmentproject.domain.model.UserDetail

fun UserDto.toEntity() = UserEntity(
    id = id,
    login = login,
    avatarUrl = avatarUrl
)

fun UserEntity.toDomain() = User(
    id = id,
    username = login,
    avatarUrl = avatarUrl
)

fun UserDetailDto.toEntity() = UserDetailEntity(
    login = login,
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    bio = bio,
    company = company,
    location = location,
    blog = blog,
    publicRepos = publicRepos,
    followers = followers,
    following = following
)

fun UserDetailEntity.toDomain() = UserDetail(
    id = id,
    username = login,
    name = name,
    avatarUrl = avatarUrl,
    bio = bio,
    company = company,
    location = location,
    blog = blog,
    publicRepos = publicRepos,
    followers = followers,
    following = following
)