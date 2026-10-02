package com.example.androidassestmentproject.data.repository

import com.example.androidassestmentproject.data.local.UserDao
import com.example.androidassestmentproject.data.local.entity.UserEntity
import com.example.androidassestmentproject.data.mapper.toDomain
import com.example.androidassestmentproject.data.mapper.toEntity
import com.example.androidassestmentproject.data.remote.GithubApiService
import com.example.androidassestmentproject.domain.model.ErrorType
import com.example.androidassestmentproject.domain.model.Resource
import com.example.androidassestmentproject.domain.model.User
import com.example.androidassestmentproject.domain.model.UserDetail
import com.example.androidassestmentproject.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: GithubApiService,
    private val userDao: UserDao
) : UserRepository {
    override suspend fun getUsers(): Resource<List<User>> {
        return try {
            val users = api.getUsers().map { it.toEntity() }
            userDao.upsertUsers(users)
            Resource.Success(users.map { it.toDomain() })
        } catch (e: IOException) {
            userDao.getUsers().toCachedResult(ErrorType.NO_CONNECTION)
        } catch (e: HttpException) {
            userDao.getUsers().toCachedResult(e.toErrorType())
        }
    }

    override suspend fun searchUsers(query: String): Resource<List<User>> {
        return try {
            val users = api.searchUsers(query).items.map { it.toEntity() }
            userDao.upsertUsers(users)
            Resource.Success(users.map { it.toDomain() })
        } catch (e: IOException) {
            userDao.searchUsers(query).toCachedResult(ErrorType.NO_CONNECTION)
        } catch (e: HttpException) {
            userDao.searchUsers(query).toCachedResult(e.toErrorType())
        }
    }

    override fun getUserDetail(username: String): Flow<Resource<UserDetail>> = flow {
        val cachedDetail = userDao.getUserDetail(username)?.toDomain()
        if (cachedDetail != null) {
            emit(Resource.Success(cachedDetail))
        }

        val remoteResult = try {
            val userDetail = api.getUserDetail(username).toEntity()
            userDao.upsertUserDetail(userDetail)
            Resource.Success(userDetail.toDomain())
        } catch (e: IOException) {
            Resource.Error(ErrorType.NO_CONNECTION)
        } catch (e: HttpException) {
            Resource.Error(e.toErrorType())
        }

        when {
            remoteResult is Resource.Success -> emit(remoteResult)
            cachedDetail != null -> emit(Resource.Success(cachedDetail, fromCache = true))
            else -> emit(remoteResult)
        }
    }


    private fun List<UserEntity>.toCachedResult(errorType: ErrorType): Resource<List<User>> {
        return if (isEmpty()) {
            Resource.Error(errorType)
        } else {
            Resource.Success(map { it.toDomain() }, fromCache = true)
        }
    }

    
    private fun HttpException.toErrorType(): ErrorType = when (code()) {
        403, 429 -> ErrorType.RATE_LIMITED
        404 -> ErrorType.NOT_FOUND
        422 -> ErrorType.INVALID_QUERY
        else -> ErrorType.UNKNOWN
    }
}