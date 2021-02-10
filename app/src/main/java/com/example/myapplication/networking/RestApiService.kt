package com.example.myapplication.networking

import com.example.myapplication.model.User
import com.example.myapplication.model.UserCreateResponse
import com.example.myapplication.model.UserResponse
import okhttp3.ResponseBody
import org.koin.core.KoinComponent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService : KoinComponent {

    private val retrofit = ServiceBuilder.buildService(ApiEndpointsInterface::class.java)

    fun getUsers(page: Int, onResult: (Boolean, UserResponse?) -> Unit) {
        retrofit.getUsers(page).enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                onResult(false, null)
            }

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val userResponse: UserResponse = response.body()!!
                    onResult(true, userResponse)
                } else {
                    onResult(false, null)
                }
            }

        })
    }

    fun postUser(user: User, onResult: (Boolean, User) -> Unit) {
        retrofit.postUser(user).enqueue(object : Callback<UserCreateResponse> {
            override fun onFailure(call: Call<UserCreateResponse>, t: Throwable) {
                onResult(false, User())
            }

            override fun onResponse(
                call: Call<UserCreateResponse>,
                response: Response<UserCreateResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val userResponse: UserCreateResponse = response.body()!!

                    onResult(true, userResponse.data)
                } else {
                    onResult(false, User())
                }
            }

        })
    }

    fun deleteUser(id: Int, onResult: (Boolean) -> Unit) {
        retrofit.deleteUser(id).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onResult(false)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    onResult(true)
                } else {
                    onResult(false)
                }
            }

        })
    }

}