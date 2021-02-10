package com.example.myapplication.networking

import com.example.myapplication.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.KoinComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder : KoinComponent {

    private val BASE_URL = "https://gorest.co.in/public-api/"

    private val client = OkHttpClient().newBuilder()
        .addInterceptor { chain ->
            val original: Request = chain.request()

            val newRequest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader(
                    "Authorization",
                    "Bearer e0f0ba5371ffe1146589c3bd719e2c186e571cb1e4cb0b7b3f62e2380f1af628"
                )
                .addHeader("Accept", "application/json")
                .method(original.method, original.body)
                .build()
            chain.proceed(newRequest)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}