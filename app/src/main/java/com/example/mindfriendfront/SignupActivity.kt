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
import android.widget.*

class SignupActivity : AppCompatActivity() {
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

        val pwEditText = findViewById<EditText>(R.id.pw)
        val pwConfirmEditText = findViewById<EditText>(R.id.pwConfirm)
        val signUpBtn = findViewById<Button>(R.id.signUpBtn)
        dupliButton.setBackgroundColor(Color.parseColor("#7ea9fd"))
        signUpBtn.isEnabled=false
        pwConfirmEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val pw = pwEditText.text.toString()
                val pwConfirm = s.toString()

                if (pw == pwConfirm) {
                    signUpBtn.isEnabled = true
                } else {
                    signUpBtn.isEnabled = false
                }
            }
        })

    }

    companion object {
        const val REQUEST_IMAGE_GALLERY = 1
    }
}