package com.example.mindfriendfront.api


import com.example.mindfriendfront.data.UserLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/sign-in")
    fun loginUser(@Body userData: UserLogin): Call<Void>
}
