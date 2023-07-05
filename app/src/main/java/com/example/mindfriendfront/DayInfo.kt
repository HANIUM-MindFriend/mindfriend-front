package com.example.mindfriendfront

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DayInfo : AppCompatActivity() {
    private lateinit var btnBack: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_info)

//        btnMenu = findViewById(R.id.btnMenu)
//        btnBack = findViewById(R.id.btnBack)

        // 메뉴 열기 버튼 클릭 시 동작 추가
//        btnBack.setOnClickListener {
//            Toast.makeText(applicationContext, "버튼 클릭", Toast.LENGTH_SHORT).show()
//        }

//        btnBack = findViewById(R.id.btnBack)
//        btnBack.setOnClickListener {
//            Toast.makeText(applicationContext, "버튼 클릭", Toast.LENGTH_SHORT).show()
//        }
    }
}