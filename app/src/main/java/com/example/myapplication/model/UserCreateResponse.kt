package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class UserCreateResponse(

    @SerializedName("code") val code: Int,
    @SerializedName("meta") val meta: Meta,
    @SerializedName("data") val data: User
)