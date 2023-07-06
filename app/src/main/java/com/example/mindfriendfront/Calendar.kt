package com.example.mindfriendfront

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.CalendarView
import java.util.Calendar
import android.util.Log
import android.widget.Button
import android.view.View
import android.widget.Toast

class Calendar : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var writeBtn: ImageButton
    private lateinit var readBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        calendarView = findViewById(R.id.calendarView)
        writeBtn = findViewById(R.id.writeBtn)
        readBtn = findViewById(R.id.readBtn)

        // 배경색 동적 변경
        val desiredColor = Color.parseColor("#0C2A64") // 원하는 색상으로 변경
        readBtn.setBackgroundColor(desiredColor)

        // 이미지 버튼 크기를 이미지의 크기에 맞게 조정
        val writeDrawable = resources.getDrawable(R.drawable.write)
        writeBtn.layoutParams.width = writeDrawable.intrinsicWidth
        writeBtn.layoutParams.height = writeDrawable.intrinsicHeight

        // 초기에는 readBtn만 표시하고 writeBtn은 숨김
        writeBtn.visibility = View.VISIBLE
        readBtn.visibility = View.GONE

        // CalendarView의 날짜 선택 이벤트 리스너 설정
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)

            val todayCalendar = Calendar.getInstance()

            if (selectedCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
                selectedCalendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH) &&
                selectedCalendar.get(Calendar.DAY_OF_MONTH) == todayCalendar.get(Calendar.DAY_OF_MONTH)
            ) {
                writeBtn.visibility = View.VISIBLE
                readBtn.visibility = View.GONE
            } else {
                writeBtn.visibility = View.GONE
                readBtn.visibility = View.VISIBLE
            }
        }

        // writeBtn 클릭 이벤트 설정
        writeBtn.setOnClickListener {
            // writeBtn 클릭 시 동작 수행
            Toast.makeText(applicationContext, "일기 쓰기", Toast.LENGTH_SHORT).show()
        }

        // readBtn 클릭 이벤트 설정
        readBtn.setOnClickListener {
            // readBtn 클릭 시 동작 수행
            Toast.makeText(applicationContext, "일기 조회", Toast.LENGTH_SHORT).show()
        }



    }
}


