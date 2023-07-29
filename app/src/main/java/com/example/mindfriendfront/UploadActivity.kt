package com.example.mindfriendfront

import android.content.Context
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    // "http://localhost:8080/diary/{id}"에 GET 요청을 보내고, 응답으로 DiaryResponse를 받음
    @GET("diary/{id}")
    fun getDiary(@Path("id") id: Int): Call<DiaryResponse>
}

data class DiaryResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: DiaryData
)

data class DiaryData(
    @SerializedName("profileImg") val profileImg: String?,
    @SerializedName("diaryIdx") val diaryIdx: Int,
    @SerializedName("title") val title: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("content") val content: String,
    @SerializedName("image") val image: String,
    @SerializedName("emotion") val emotion: String
)

class UploadActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        val dateTextView: TextView = findViewById(R.id.date)
        val cameraImageButton: ImageButton = findViewById(R.id.camera_button)
        val galleryImageButton: ImageButton = findViewById(R.id.gallery_button)
        val playerImageButton: ImageButton = findViewById(R.id.player_button)
        val uploadImageButton: ImageButton = findViewById(R.id.write_ib_upload)

        val currentDate = Date()

        val dateFormat = SimpleDateFormat("yyyy. MM. dd")
        val formattedDate = dateFormat.format(currentDate)

        dateTextView.text = formattedDate

        // 일기 단건 조회 API 호출
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)
        val diaryId = 2
        val call = service.getDiary(diaryId)

        call.enqueue(object : Callback<DiaryResponse> {
            override fun onResponse(call: Call<DiaryResponse>, response: Response<DiaryResponse>) {
                if (response.isSuccessful) {
                    val diaryResponse = response.body()
                    diaryResponse?.let {
                        val emotion = it.data.emotion
                        if (emotion == "행복") {
                            // emotion이 "행복"일 경우에 해당 음악을 틀기
                            Toast.makeText(applicationContext, "행복한 상태니 dreams 음악을 틀어드릴게요,", Toast.LENGTH_SHORT).show()
                            Log.d("HAPPY","행복한 상태니 dreams 음악을 틀어드릴게요.")
                            mediaPlayer = MediaPlayer.create(this@UploadActivity, R.raw.dreams)

                        } else if (emotion == "우울") {
                            // emotion이 "우울"일 경우에 해당 음악을 틀기
                            Toast.makeText(applicationContext, "우울한 상태니 pianomoment 음악을 틀어드릴게요.", Toast.LENGTH_SHORT).show()
                            Log.d("DEPRESSED","행복한 상태니 dreams 음악을 틀어드릴게요.")
                            mediaPlayer = MediaPlayer.create(this@UploadActivity, R.raw.pianomoment)

                        } else {
                            Toast.makeText(applicationContext, "일반 상태니 lawrence 음악을 틀어드릴게요.", Toast.LENGTH_SHORT).show()
                            Log.d("NOMAL","일반 상태니 lawrence 음악을 틀어드릴게요.")
                            mediaPlayer = MediaPlayer.create(this@UploadActivity, R.raw.lawrence)
                        }
                    }
                } else {
                    Toast.makeText(applicationContext, "일기 조회에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("FAIL","일기 조회에 실패했습니다.")
                    mediaPlayer = MediaPlayer.create(this@UploadActivity, R.raw.lawrence)
                }
            }

            override fun onFailure(call: Call<DiaryResponse>, t: Throwable) {
                // 네트워크 오류 처리
                Toast.makeText(applicationContext, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                Log.d("FAIL","네트워크 오류가 발생했습니다.")
                mediaPlayer = MediaPlayer.create(this@UploadActivity, R.raw.lawrence)
            }
        })



    cameraImageButton.setOnClickListener {
            // 카메라 버튼 클릭 시 반응
            Toast.makeText(applicationContext, "카메라 호출", Toast.LENGTH_SHORT).show()
        }
        galleryImageButton.setOnClickListener {
            // 갤러리 버튼 클릭 시 반응
            Toast.makeText(applicationContext, "갤러리 호출", Toast.LENGTH_SHORT).show()
        }

        uploadImageButton.setOnClickListener {
            // 작성 버튼 클릭 시 반응
        }



        playerImageButton.setOnClickListener {
            if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                stopAudio()
                playerImageButton.setImageResource(R.drawable.player_on)
            } else {
                //mediaPlayer = MediaPlayer.create(this, R.raw.dreams)
                mediaPlayer?.setOnCompletionListener {
                    stopAudio()
                    playerImageButton.setImageResource(R.drawable.player_on)
                }
                playAudio()
                playerImageButton.setImageResource(R.drawable.player_off)
            }
        }
    }

    private fun playAudio() {
        if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
            mediaPlayer?.start()
        }
    }

    private fun stopAudio() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
