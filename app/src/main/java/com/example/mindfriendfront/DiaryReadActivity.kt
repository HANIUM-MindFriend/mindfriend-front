package com.example.mindfriendfront

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mindfriendfront.data.DiaryGetResponse
import com.example.mindfriendfront.network.ApiServiceFactory
import com.google.gson.JsonObject
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

class DiaryReadActivity :AppCompatActivity () {

    companion object {
        fun newInstance(): DiaryReadActivity {
            return DiaryReadActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        // Intent로 전달된 데이터 가져오기
        val selectedDateMillis = intent.getLongExtra("selectedDate", 0L) // 0L은 기본값으로 사용됩니다.

        val selectedDate = Date(selectedDateMillis)
        Log.e("Diarydate", selectedDate.toString())

        val dateTextView: TextView = findViewById(R.id.date)
        val diaryText: TextView = findViewById(R.id.uploadText)
        val profile : CircleImageView = findViewById(R.id.circle_profile)



        val dateFormat = SimpleDateFormat("MMdd")
        val formattedDate = dateFormat.format(selectedDate)

        val queryDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val queryFormattedDate = queryDateFormat.format(selectedDate)
        Log.e("QueryDiarydate", queryFormattedDate)
        dateTextView.text = formattedDate
        // ApiServiceFactory를 통해 ApiService 인스턴스 생성
        val apiService = ApiServiceFactory.apiService


        apiService.getDiary(date = queryFormattedDate).enqueue(object : Callback<DiaryGetResponse> {
            override fun onResponse(call: Call<DiaryGetResponse>, response: Response<DiaryGetResponse>) {
                if (response.isSuccessful) {
                    val diaryResponse = response.body()
                    val diaryData = diaryResponse?.data

                    diaryData?.let {
                        diaryText.text = it.content // 일기 내용 설정
                        // 이미지 라이브러리를 사용하여 profile 이미지 설정
                        Glide.with(this@DiaryReadActivity)
                            .load(diaryData.profileImg)
                            .into(profile)
                    }
                } else {
                    // 오류 처리
                    val errorBody = response.errorBody()?.string()
                    val message = "응답 코드: ${response.code()}, 메시지: ${response.message()}, 오류 내용: $errorBody"
                    Log.e("API_RESPONSE", message)
                    Toast.makeText(applicationContext, "오류가 발생했습니다. Error Code: "+response.code(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DiaryGetResponse>, t: Throwable) {
                // 네트워크 오류 처리
                Log.e("API_RESPONSE", "네트워크 실패: ${t.message}")
                Toast.makeText(applicationContext, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        })

    }
}