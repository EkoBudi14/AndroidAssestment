package com.example.androidassestmentproject.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_details")
data class UserDetailEntity(
    @PrimaryKey val login: String,
    val id: Long,
    val name: String?,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String,
    val bio: String?,
    val company: String?,
    val location: String?,
    val blog: String?,
    @ColumnInfo(name = "public_repos") val publicRepos: Int,
    val followers: Int,
    val following: Int
)