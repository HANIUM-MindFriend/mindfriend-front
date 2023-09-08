package com.example.mindfriendfront.api


import com.example.mindfriendfront.data.CalendarResponse
import com.example.mindfriendfront.data.DiaryGetResponse
import com.example.mindfriendfront.data.DiarySingleResponse
import com.example.mindfriendfront.data.DiaryUpload
import com.example.mindfriendfront.data.DuplicateResponse
import com.example.mindfriendfront.data.LoginResponse
import com.example.mindfriendfront.data.UserDuplicate
import com.example.mindfriendfront.data.UserSignUp
import com.example.mindfriendfront.data.UserLogin
import com.example.mindfriendfront.data.DashboardResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("users/duplicate")
    fun chkDuplicate(@Body userData: UserDuplicate) : Call<DuplicateResponse>
    @POST("auth/sign-in")
    fun loginUser(@Body userData: UserLogin): Call<LoginResponse>

    @Multipart
    @POST("diary/write/emo")
    fun uploadSingleDiary(
        @Part("postAiDiary") content: RequestBody
    ): Call<DiarySingleResponse>

    @GET("diary/date/search")
    fun getCalendar(@Query("yearMonth") yearMonth: String): Call<CalendarResponse>

    @GET("diary/read")
    fun getDiary(@Query("date") date: String): Call<DiaryGetResponse>

    @GET("diary/dash")
    fun getDashboard(@Query("yearMonth") yearMonth: String): Call<DashboardResponse>

    @Multipart
    @POST("/auth/sign-up")
    fun signUp(
        @Part profileImg: MultipartBody.Part,
        @Part("signUp") userSignUp: UserSignUp
    ): Call<ResponseBody>

    @Multipart
    @POST("/diary/write")
    fun uploadDiary(
        @Part postImg: MultipartBody.Part,
        @Part("postDiary") diaryUpload: DiaryUpload
    ): Call<ResponseBody>
}
