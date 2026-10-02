package com.example.androidassestmentproject.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.androidassestmentproject.data.local.entity.UserDetailEntity
import com.example.androidassestmentproject.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Upsert
    suspend fun upsertUsers(users: List<UserEntity>)

    @Query("SELECT * FROM users ORDER BY id ASC")
    suspend fun getUsers(): List<UserEntity>

    @Query("SELECT * FROM users WHERE login LIKE '%' || :query || '%' ORDER BY login ASC")
    suspend fun searchUsers(query: String): List<UserEntity>

    @Upsert
    suspend fun upsertUserDetail(userDetail: UserDetailEntity)

    @Query("SELECT * FROM user_details WHERE login = :username COLLATE NOCASE LIMIT 1")
    suspend fun getUserDetail(username: String): UserDetailEntity?
}