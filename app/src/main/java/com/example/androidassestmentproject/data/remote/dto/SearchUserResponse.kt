package com.example.androidassestmentproject.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("items") val items: List<UserDto>
)