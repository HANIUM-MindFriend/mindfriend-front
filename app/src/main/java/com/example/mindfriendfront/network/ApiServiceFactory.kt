// ApiServiceFactory.kt 파일
package com.example.mindfriendfront.network

import android.content.Context
import com.example.mindfriendfront.MyApplication
import com.example.mindfriendfront.api.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceFactory {
    private const val BASE_URL = "http://10.0.2.2:8080"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(createOkHttpClient()) // OkHttpClient 추가
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)

    // Retrofit Interceptor를 사용하여 모든 요청에 토큰 추가하는 OkHttpClient 생성
    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val sharedPreferences = MyApplication.context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
            val accessToken = sharedPreferences.getString("accessToken", "")

            if (accessToken.isNullOrEmpty()) {
                // 토큰이 없는 경우에는 그대로 요청을 진행
                chain.proceed(original)
            } else {
                // 토큰이 있는 경우에는 요청 헤더에 토큰 추가
                val request = original.newBuilder()
                    .header("Authorization", "Bearer $accessToken")
                    .build()
                chain.proceed(request)
            }
        }.build()
    }
}
