package com.example.mindfriendfront
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var btnMenu: Button
    private lateinit var btnAnalysis: Button
    private lateinit var btnHome: Button
    private lateinit var btnMyPage: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnMenu = findViewById(R.id.btnMenu)
        btnAnalysis = findViewById(R.id.btnAnalysis)
        btnHome = findViewById(R.id.btnHome)
        btnMyPage = findViewById(R.id.btnMyPage)

        // 메뉴 열기 버튼 클릭 시 동작 추가
        btnMenu.setOnClickListener {
            // 메뉴를 열기 위한 동작 구현

        }

        // 분석 버튼 클릭 시 동작 추가
        btnAnalysis.setOnClickListener {
            // 분석 화면으로 이동하는 동작 구현
            setContentView(R.layout.activity_chat)
        }

        // 홈 버튼 클릭 시 동작 추가
        btnHome.setOnClickListener {
            Toast.makeText(applicationContext, "홈 클릭", Toast.LENGTH_SHORT).show()
        }

        // 마이페이지 버튼 클릭 시 동작 추가
        btnMyPage.setOnClickListener {
            // 마이페이지로 이동하는 동작 구현
        }

        btnAnalysis.setBackgroundColor(Color.parseColor("#7ea9fd"))
        btnHome.setBackgroundColor(Color.parseColor("#7ea9fd"))
        btnMyPage.setBackgroundColor(Color.parseColor("#7ea9fd"))
        btnMenu.setCompoundDrawablesWithIntrinsicBounds(R.drawable.menu, 0, 0, 0)
        btnAnalysis.setCompoundDrawablesWithIntrinsicBounds(R.drawable.analysis, 0, 0, 0)
        btnHome.setCompoundDrawablesWithIntrinsicBounds(R.drawable.home, 0, 0, 0)
        btnMyPage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mypage, 0, 0, 0)
// 버튼의 배경색 설정 예시 (AndroidX 라이브러리 사용)




    }
}
