package com.example.mindfriendfront

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.media.MediaPlayer
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Upload_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Upload_fragment : Fragment() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var imageView: ImageView

    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2
    private var imageFilePath: String? = null
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
            if (checkCameraPermission()) {
                openCamera()
            } else {
                requestCameraPermission()
            }
            Toast.makeText(requireContext(), "카메라 호출", Toast.LENGTH_SHORT).show()
        }

        galleryImageButton.setOnClickListener {
            // 갤러리 버튼 클릭 시 반응
            if (checkGalleryPermission()) {
                openGallery()
            } else {
                requestGalleryPermission()
            }
            Toast.makeText(requireContext(), "갤러리 호출", Toast.LENGTH_SHORT).show()
        }

        uploadImageButton.setOnClickListener {
            // 작성 버튼 클릭 시 반응
            val Analysis_fragment = Analysis_fragment.newInstance()

            childFragmentManager.beginTransaction()
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
    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }


    private fun checkGalleryPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestGalleryPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            GALLERY_REQUEST_CODE
        )
    }


    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val imageFile = createImageFile()
        imageFile?.let {
            val imageUri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.mindfriendfront.fileprovider",
                it)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }
    }
    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            imageFilePath = absolutePath
        }
    }



    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val imageBitmap = BitmapFactory.decodeFile(imageFilePath)
                    imageView.setImageBitmap(imageBitmap)
                }
                GALLERY_REQUEST_CODE -> {
                    val selectedImageUri = data?.data
                    imageView.setImageURI(selectedImageUri)
                }
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                }
            }
            GALLERY_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                }
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