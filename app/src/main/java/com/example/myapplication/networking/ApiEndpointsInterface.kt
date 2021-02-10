package com.example.myapplication.networking

import com.example.myapplication.model.User
import com.example.myapplication.model.UserCreateResponse
import com.example.myapplication.model.UserResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiEndpointsInterface {
    @GET("users")
    fun getUsers(@Query("page") page: Int): Call<UserResponse>

    @POST("users")
    fun postUser(@Body() user: User): Call<UserCreateResponse>

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") id: Int): Call<ResponseBody>

}