package com.example.mindfriendfront.api


import com.example.mindfriendfront.data.DuplicateResponse
import com.example.mindfriendfront.data.LoginResponse
import com.example.mindfriendfront.data.UserDuplicate
import com.example.mindfriendfront.data.UserSignUp
import com.example.mindfriendfront.data.UserLogin
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("users/duplicate")
    fun chkDuplicate(@Body userData: UserDuplicate) : Call<DuplicateResponse>
    @POST("auth/sign-in")
    fun loginUser(@Body userData: UserLogin): Call<LoginResponse>



    @Multipart
    @POST("/auth/sign-up")
    fun signUp(
        @Part profileImg: MultipartBody.Part,
        @Part("signUp") userSignUp: UserSignUp
    ): Call<ResponseBody>
}
