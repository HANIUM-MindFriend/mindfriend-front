// network/ApiServiceFactory.kt
package com.example.mindfriendfront.network

import com.example.mindfriendfront.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceFactory {
    private const val BASE_URL = "http://43.202.83.116:8080/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
