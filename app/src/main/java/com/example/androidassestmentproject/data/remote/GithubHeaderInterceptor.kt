package com.example.androidassestmentproject.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class GithubHeaderInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Accept", "application/vnd.github+json")
            .apply { if (token.isNotBlank()) header("Authorization", "Bearer $token") }
            .build()
        return chain.proceed(request)
    }
}