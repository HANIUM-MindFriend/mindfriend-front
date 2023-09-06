package com.example.mindfriendfront

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.media.MediaPlayer
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.mindfriendfront.data.DiarySingle
import com.example.mindfriendfront.data.DiarySingleResponse
import com.example.mindfriendfront.data.DiaryUpload
import com.example.mindfriendfront.data.LoginResponse
import com.example.mindfriendfront.data.UserLogin
import com.example.mindfriendfront.data.UserSignUp
import com.example.mindfriendfront.network.ApiServiceFactory
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
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
    private lateinit var title : EditText
    private lateinit var content : EditText
    private lateinit var relativeLayout: RelativeLayout
    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2
    private var imageFilePath: String? = null
    private var previousText: String = ""



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
        imageView = rootView.findViewById(R.id.imageView) // ImageView 초기화 추가
        val dateTextView: TextView = rootView.findViewById(R.id.date)
        relativeLayout = rootView.findViewById(R.id.relativeLayout)
        title= rootView.findViewById(R.id.editTitle)
        content = rootView.findViewById(R.id.editContent)
        val cameraImageButton: ImageButton = rootView.findViewById(R.id.camera_button)
        val galleryImageButton: ImageButton = rootView.findViewById(R.id.gallery_button)
        val playerImageButton: ImageButton = rootView.findViewById(R.id.player_button)
        val uploadButton: ImageButton = rootView.findViewById(R.id.write_ib_upload)

        val currentDate = Date()

        val dateFormat = SimpleDateFormat("yyyy. MM. dd")
        val formattedDate = dateFormat.format(currentDate)


        dateTextView.text = formattedDate

        content.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val currentText = content.text.toString()
                if (currentText.isNotEmpty()) {
                    val newText = "$currentText\n" // 현재 텍스트에 엔터를 추가하여 다음 줄로 넘어가도록 합니다.
                    content.text.clear() // 기존 텍스트를 지우고
                    content.append(newText) // 새로운 텍스트를 추가합니다.
                    content.setSelection(content.length()) // 커서를 텍스트 끝으로 이동시켜 다음 줄에 텍스트를 입력할 수 있게 합니다.
                    val textLines = currentText.split("\n") // 엔터(\n)로 텍스트를 나눕니다.
                    if (textLines.isNotEmpty()) {
                        val lastLine = textLines.last() // 마지막 줄을 추출합니다.
                        uploadSingleDiaryRequest(lastLine) // 마지막 줄을 업로드합니다.
                    }

                }
                true
            } else {
                false
            }
        }




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

        uploadButton.setOnClickListener {
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
                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.thejazzpiano)
                mediaPlayer?.setOnCompletionListener {
                    stopAudio()
                    playerImageButton.setImageResource(R.drawable.player_on)
                }
                playAudio()
                playerImageButton.setImageResource(R.drawable.player_off)
            }
        }

        uploadButton.setOnClickListener {
            uploadDiaryRequest()
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

    private fun uploadDiaryRequest() {
        // 프로필 이미지 파일을 MultipartBody.Part로 변환
        // imageView에서 이미지 가져오기
        val imageViewDrawable = imageView.drawable
        if (imageViewDrawable == null) {
            Toast.makeText(requireContext(), "이미지를 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // imageView의 drawable을 Bitmap으로 변환
        val imageBitmap = (imageViewDrawable as BitmapDrawable).bitmap

        // Bitmap을 File로 변환
        val postImgFile = createImageFile()
        postImgFile?.let {
            FileOutputStream(it).apply {
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, this)
                close()
            }
        }
        if (postImgFile == null) {
            Toast.makeText(requireContext(), "프로필 이미지를 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        val postImgRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), postImgFile)
        val postImgPart = MultipartBody.Part.createFormData("postImg", postImgFile.name, postImgRequestBody)

        // 회원가입 정보를 데이터 모델 클래스로 생성
        val diaryUpload = DiaryUpload(
            title = title.text.toString(),
            content = content.text.toString(),
        )

        // 회원가입 정보를 JSON 형태의 RequestBody로 변환
        val signUpRequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), diaryUpload.toString())

        // ApiService를 가져와서 요청 보내기
        val apiService = ApiServiceFactory.apiService
        apiService.uploadDiary(postImgPart, diaryUpload).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // 성공적으로 응답 받았을 때 처리
                    Toast.makeText(requireContext(), "일기 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    // 응답은 성공적으로 받았지만 서버에서 오류 응답을 보낸 경우 처리
                    val message = "응답 코드: ${response.code()}, 메시지: ${response.message()}"
                    Log.e("API_RESPONSE", message)
                    Toast.makeText(requireContext(), "서버 오류: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 요청 실패 시 처리

                Log.e("API_RESPONSE", "네트워크 실패: ${t.message}")
                Toast.makeText(requireContext(), "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uploadSingleDiaryRequest(diaryLine: String) {
        val json = "{\"content\":\"$diaryLine\"}"
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json)
        val content = "{\"content\":\"$diaryLine\"}".toRequestBody("application/json".toMediaTypeOrNull())

        val call = ApiServiceFactory.apiService.uploadSingleDiary(content)

        call.enqueue(object : Callback<DiarySingleResponse> {
            override fun onResponse(call: Call<DiarySingleResponse>, response: Response<DiarySingleResponse>) {
                if (response.isSuccessful) {
                    val singleResponse = response.body()
                    if (singleResponse != null) {
                        Toast.makeText(requireContext(), diaryLine + ": " + singleResponse.data.emotion, Toast.LENGTH_SHORT).show()
                        if (singleResponse.data.emoIdx == 1){
                            //분노
                            Log.e("Emo", "angry")
                            relativeLayout?.setBackgroundColor(Color.parseColor("#D4F0F0")) //하늘
                            //pianomoment
                            if (mediaPlayer != null && mediaPlayer!!.isPlaying){
                                mediaPlayer?.release()
                                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.pianomoment)
                                mediaPlayer?.start()
                            }


                        }
                        else if (singleResponse.data.emoIdx == 2){
                            //혐오
                            Log.e("Emo", "disgust")
                            relativeLayout?.setBackgroundColor(Color.parseColor("#CCE2CB")) //초록
                            //onceagain
                            if (mediaPlayer != null && mediaPlayer!!.isPlaying){
                                mediaPlayer?.release()
                                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.onceagain)
                                mediaPlayer?.start()
                            }


                        }
                        else if (singleResponse.data.emoIdx == 3){
                            //두려움
                            relativeLayout?.setBackgroundColor(Color.parseColor("#B6C1A9")) //찐초록
                            //tenderness
                            if (mediaPlayer != null && mediaPlayer!!.isPlaying){
                                mediaPlayer?.release()
                                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.tenderness)
                                mediaPlayer?.start()
                            }


                        }
                        else if (singleResponse.data.emoIdx == 4){
                            //행복
                            relativeLayout?.setBackgroundColor(Color.parseColor("#FF968A")) //빨강
                            //dreams
                            if (mediaPlayer != null &&  mediaPlayer!!.isPlaying){
                                mediaPlayer?.release()
                                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.dreams)
                                mediaPlayer?.start()
                            }

                        }
                        else if (singleResponse.data.emoIdx == 5){
                            //중립
                            relativeLayout?.setBackgroundColor(Color.parseColor("#F2F5FF")) //원래
                            //thejazzpaino
                            if (mediaPlayer != null && mediaPlayer!!.isPlaying){
                                mediaPlayer?.release()
                                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.thejazzpiano)
                                mediaPlayer?.start()
                            }

                        }
                        else if (singleResponse.data.emoIdx == 6){
                            //슬픔
                            relativeLayout?.setBackgroundColor(Color.parseColor("#FFD8BE")) //주황
                            //lawrence
                            if (mediaPlayer != null && mediaPlayer!!.isPlaying){
                                mediaPlayer?.release()
                                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.lawrence)
                                mediaPlayer?.start()
                            }

                        }
                        else{
                            //놀람
                            relativeLayout?.setBackgroundColor(Color.parseColor("#F6EAC2")) //노랑
                            //love
                            if (mediaPlayer != null && mediaPlayer!!.isPlaying){
                                mediaPlayer?.release()
                                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.love)
                                mediaPlayer?.start()
                            }


                        }
                    }
                } else {
                    // 실패한 응답 처리
                    val errorBody = response.errorBody()?.string()
                    val message = "응답 코드: ${response.code()}, 메시지: ${response.message()}, 오류 내용: $errorBody"
                    Log.e("API_RESPONSE", message)
                    Toast.makeText(requireContext(), "감성 분석 요청에 실패하셨습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DiarySingleResponse>, t: Throwable) {
                // 네트워크 실패 처리
                Log.e("API_RESPONSE", "네트워크 실패: ${t.message}")
                Toast.makeText(requireContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }



}