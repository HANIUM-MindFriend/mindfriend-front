// network/ApiServiceFactory.kt
package com.example.mindfriendfront.network

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.mindfriendfront.api.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceFactory {
    private const val BASE_URL = "http://10.0.2.2:8080"
    private lateinit var context: Context

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)

    fun initialize(context: Context) {
        this.context = context
    }

    // 토큰을 Header에 추가하는 Interceptor
    private class TokenInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val token = getAccessTokenFromSharedPreferences()

            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()

            return chain.proceed(requestBuilder)
        }
    }

    // SharedPreferences에서 AccessToken을 가져오는 함수
    private fun getAccessTokenFromSharedPreferences(): String {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            "MyPrefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        return sharedPreferences.getString("accessToken", "") ?: ""
    }
}