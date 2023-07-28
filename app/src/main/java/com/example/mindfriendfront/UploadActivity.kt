package com.example.mindfriendfront

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date

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
                mediaPlayer = MediaPlayer.create(this, R.raw.lawrence)
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
