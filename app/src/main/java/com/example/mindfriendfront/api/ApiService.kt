package com.example.mindfriendfront.api


import com.example.mindfriendfront.data.SignUpData
import com.example.mindfriendfront.data.UserLogin
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("auth/sign-in")
    fun loginUser(@Body userData: UserLogin): Call<Void>

    @Multipart
    @POST("/auth/sign-up")
    fun signUp(
        @Part profileImg: MultipartBody.Part,
        @Part("signUp") signUpData: SignUpData
    ): Call<ResponseBody>
}
