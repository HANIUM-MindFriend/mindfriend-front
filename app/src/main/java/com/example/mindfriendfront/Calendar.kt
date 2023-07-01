package com.example.mindfriendfront

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.CalendarView
import java.util.Calendar
import android.util.Log

class Calendar : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var writeBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        calendarView = findViewById(R.id.calendarView)
        writeBtn = findViewById(R.id.writeBtn)

        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH)

        calendarView.setDate(currentDate.timeInMillis)

        // 이미지 버튼 크기를 이미지의 크기에 맞게 조정
        val writeDrawable = resources.getDrawable(R.drawable.write)
        writeBtn.layoutParams.width = writeDrawable.intrinsicWidth
        writeBtn.layoutParams.height = writeDrawable.intrinsicHeight

        // 이미지 버튼 클릭 이벤트 설정 등 추가 동작 구현
        writeBtn.setOnClickListener {
            Log.d("Calendar", "Button clicked")
            // 원하는 동작을 수행
        }
    }
}
