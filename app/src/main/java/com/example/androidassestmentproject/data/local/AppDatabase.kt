package com.example.androidassestmentproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidassestmentproject.data.local.entity.UserDetailEntity
import com.example.androidassestmentproject.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, UserDetailEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "github_user.db"
    }
}