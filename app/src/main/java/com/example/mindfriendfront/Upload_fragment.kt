package com.example.mindfriendfront

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.media.MediaPlayer
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Upload_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Upload_fragment : Fragment() {
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        fun newInstance(): Upload_fragment {
            return Upload_fragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_upload, container, false)

        val dateTextView: TextView = rootView.findViewById(R.id.date)
        val cameraImageButton: ImageButton = rootView.findViewById(R.id.camera_button)
        val galleryImageButton: ImageButton = rootView.findViewById(R.id.gallery_button)
        val playerImageButton: ImageButton = rootView.findViewById(R.id.player_button)
        val uploadImageButton: ImageButton = rootView.findViewById(R.id.write_ib_upload)

        val currentDate = Date()

        val dateFormat = SimpleDateFormat("yyyy. MM. dd")
        val formattedDate = dateFormat.format(currentDate)

        dateTextView.text = formattedDate

        cameraImageButton.setOnClickListener {
            // 카메라 버튼 클릭 시 반응
            Toast.makeText(requireContext(), "카메라 호출", Toast.LENGTH_SHORT).show()
        }

        galleryImageButton.setOnClickListener {
            // 갤러리 버튼 클릭 시 반응
            Toast.makeText(requireContext(), "갤러리 호출", Toast.LENGTH_SHORT).show()
        }

        uploadImageButton.setOnClickListener {
            // 작성 버튼 클릭 시 반응
            val Analysis_fragment = Analysis_fragment.newInstance()

            parentFragmentManager.beginTransaction()
                .replace(R.id.mainNaviFragmentContainer, Analysis_fragment)
                .addToBackStack(null)
                .commit()

            // 이 부분에 작성 버튼이 눌렸을 때의 동작을 구현해야 합니다.
            // 예를 들면 일기 작성을 위한 화면으로 전환하는 코드를 추가할 수 있습니다.
        }

        playerImageButton.setOnClickListener {
            if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                stopAudio()
                playerImageButton.setImageResource(R.drawable.player_on)
            } else {
                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.lawrence)
                mediaPlayer?.setOnCompletionListener {
                    stopAudio()
                    playerImageButton.setImageResource(R.drawable.player_on)
                }
                playAudio()
                playerImageButton.setImageResource(R.drawable.player_off)
            }
        }

        return rootView
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