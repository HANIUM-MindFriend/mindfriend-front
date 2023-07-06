package com.example.mindfriendfront

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.graphics.Color
import android.widget.ImageButton

class DayInfo : AppCompatActivity() {
    private lateinit var btnBack: ImageButton
    private lateinit var colorView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_info)

//        btnMenu = findViewById(R.id.btnMenu)
        btnBack = findViewById(R.id.btnBack)
        colorView = findViewById(R.id.colorView)

        // 메뉴 열기 버튼 클릭 시 동작 추가
        btnBack.setOnClickListener { v: View? ->
            // Toast.makeText(applicationContext, "버튼 클릭", Toast.LENGTH_SHORT).show()
            changeViewColor(colorView, "value1")
        }

//        btnBack = findViewById(R.id.btnBack)
//        btnBack.setOnClickListener {
//            Toast.makeText(applicationContext, "버튼 클릭", Toast.LENGTH_SHORT).show()
//
//        }
    }
    fun changeViewColor(view: View, data: String) {
        val color: Int = when (data) {
            "value1" -> Color.RED
            "value2" -> Color.GREEN
            "value3" -> Color.BLUE
            else -> Color.BLACK
        }
        view.setBackgroundColor(color)
//        android:background="@drawable/dayinfo_color"
        view.drawableState
    }
}