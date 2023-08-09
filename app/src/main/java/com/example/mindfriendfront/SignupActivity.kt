package com.example.mindfriendfront

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import android.graphics.*
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import android.app.Activity
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.*
import com.example.mindfriendfront.data.SignUpData
import com.example.mindfriendfront.network.ApiServiceFactory
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class SignupActivity : AppCompatActivity() {
    private var selectedProfileImage: File? = null
    private lateinit var idText: EditText
    private  lateinit var pwText: EditText
    private  lateinit var emailText: EditText
    private  lateinit var pwConfirmText: EditText
    private lateinit var nicknameText: EditText
    private  lateinit var yearText: EditText
    private  lateinit var monthText: EditText
    private  lateinit var dayText: EditText
    private fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        val outputBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(outputBitmap)

        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = Color.parseColor("#BAB399")
        canvas.drawRoundRect(rectF, bitmap.width.toFloat(), bitmap.height.toFloat(), paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return outputBitmap
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val drawableId = R.drawable.ic_menu
        val originalBitmap = BitmapFactory.decodeResource(resources, drawableId)
        idText = findViewById(R.id.idText)
        pwText = findViewById(R.id.pwText)
        emailText = findViewById(R.id.emailText)
        pwConfirmText = findViewById(R.id.pwConfirmText)
        nicknameText = findViewById(R.id.nicknameText)
        yearText = findViewById(R.id.yearText)
        monthText = findViewById(R.id.monthText)
        dayText = findViewById(R.id.dayText)





        // 이미지 자르기 함수 호출
        val circularBitmap = getCircularBitmap(originalBitmap)

        // ImageView에 원형 이미지 설정
        val profileIMG: ImageButton = findViewById(R.id.profileIMG)
        profileIMG.setOnClickListener {
            // 갤러리에서 이미지 선택
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
        }

        val dupliButton :Button=findViewById(R.id.dupliButton)
        dupliButton.setBackgroundColor(Color.parseColor("#7ea9fd"))


        val cbTotal: CheckBox =findViewById(R.id.cbTotal)
        val cb1: CheckBox =findViewById(R.id.cb1)
        val cb2: CheckBox =findViewById(R.id.cb2)
        val cb3: CheckBox =findViewById(R.id.cb3)

        fun onCheckChanged(compoundButton: CompoundButton) {
            when(compoundButton.id) {
                R.id.cbTotal -> {
                    if (cbTotal.isChecked) {
                        cb1.isChecked = true
                        cb2.isChecked = true
                        cb3.isChecked = true
                    }else {
                        cb1.isChecked = false
                        cb2.isChecked = false
                        cb3.isChecked = false
                    }
                }
                else -> {
                    cbTotal.isChecked = (
                            cb1.isChecked
                                    && cb2.isChecked
                                    && cb3.isChecked)
                }
            }
        }
        cbTotal.setOnClickListener { onCheckChanged(cbTotal) }
        cb1.setOnClickListener { onCheckChanged(cb1) }
        cb2.setOnClickListener { onCheckChanged(cb2) }
        cb3.setOnClickListener { onCheckChanged(cb3) }


        val signUpBtn = findViewById<Button>(R.id.signUpBtn)
        dupliButton.setBackgroundColor(Color.parseColor("#7ea9fd"))
        signUpBtn.isEnabled=false
        pwConfirmText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val pw = pwText.text.toString()
                val pwConfirm = s.toString()

                if (pw == pwConfirm) {
                    signUpBtn.isEnabled = true
                } else {
                    signUpBtn.isEnabled = false
                }
            }
        })
        signUpBtn.setOnClickListener {
            processDate()
        }

    }

    private fun isDateValid(dateStr: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            sdf.isLenient = false
            sdf.parse(dateStr)
            true
        } catch (e: ParseException) {
            false
        }
    }

    // 날짜 확인 후 처리하는 함수
    private fun processDate() {
        val dateStr = "${yearText.text.toString()}-${monthText.text.toString()}-${dayText.text.toString()}"
        if (isDateValid(dateStr)) {
            // 날짜가 올바른 형식일 때 처리
            // 여기서 날짜를 원하는 형식으로 사용하면 됩니다.
            sendSignUpRequest()
        } else {
            // 날짜가 올바르지 않은 형식일 때 처리
            Toast.makeText(this, "올바른 날짜 형식으로 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendSignUpRequest() {
        // 프로필 이미지 파일을 MultipartBody.Part로 변환
        val profileImgFile = selectedProfileImage // 갤러리에서 선택한 이미지 파일 사용
        if (profileImgFile == null) {
            Toast.makeText(this, "프로필 이미지를 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        val profileImgRequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), profileImgFile)
        val profileImgPart = MultipartBody.Part.createFormData("profileImg", profileImgFile.name, profileImgRequestBody)

        // 회원가입 정보를 데이터 모델 클래스로 생성
        val signUpData = SignUpData(
            userId = idText.text.toString(),
            userPassword = pwText.text.toString(),
            userNickname = nicknameText.text.toString(),
            userBirth = yearText.text.toString()+"-"+monthText.text.toString()+"-"+dayText.text.toString(),
            userEmail = emailText.text.toString()
        )

        // 회원가입 정보를 JSON 형태의 RequestBody로 변환
        val signUpRequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), signUpData.toString())

        // ApiService를 가져와서 요청 보내기
        val apiService = ApiServiceFactory.apiService
        apiService.signUp(profileImgPart, signUpData).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // 성공적으로 응답 받았을 때 처리
                    Toast.makeText(this@SignupActivity, "회원가입에 성공하셨습니다. 로그인페이지로 이동합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    // 응답은 성공적으로 받았지만 서버에서 오류 응답을 보낸 경우 처리
                    val message = "응답 코드: ${response.code()}, 메시지: ${response.message()}"
                    Log.e("API_RESPONSE", message)
                    Toast.makeText(this@SignupActivity, "서버 오류: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 요청 실패 시 처리

                Log.e("API_RESPONSE", "네트워크 실패: ${t.message}")
                Toast.makeText(this@SignupActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            // 이미지를 선택한 경우 실행될 부분
            val selectedImage = data.data
            if (selectedImage != null) {
                // 선택한 이미지를 비트맵으로 변환하여 ImageView에 설정
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                val circularBitmap = getCircularBitmap(bitmap)
                val profileIMG: ImageButton = findViewById(R.id.profileIMG)
                profileIMG.setImageBitmap(circularBitmap)

                // 갤러리에서 선택한 이미지의 URI를 사용하여 파일 가져오기
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null)
                cursor?.moveToFirst()
                val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                val imagePath = cursor?.getString(columnIndex ?: 0)
                cursor?.close()

                imagePath?.let {
                    selectedProfileImage = File(it)
                }
            } else {
                Toast.makeText(this, "이미지를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object {
        const val REQUEST_IMAGE_GALLERY = 1
    }
}