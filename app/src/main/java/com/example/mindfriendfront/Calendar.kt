package com.example.mindfriendfront

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.CalendarView
import java.util.Calendar
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Calendar : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var writeBtn: ImageButton
    private lateinit var readBtn: Button
    // 선택한 날짜를 저장할 변수
    private var selectedDate: Long = 0
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
            selectedDate = selectedCalendar.timeInMillis // 선택한 날짜를 저장


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
            val intent = Intent(applicationContext, com.example.mindfriendfront.UploadActivity::class.java)
            startActivity(intent)
        }

        // readBtn 클릭 이벤트 설정
        readBtn.setOnClickListener {
            // readBtn 클릭 시 동작 수행
            Log.e("date", selectedDate.toString())
            Toast.makeText(applicationContext, "일기 조회", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, com.example.mindfriendfront.DiaryReadActivity::class.java)
            intent.putExtra("selectedDate", selectedDate) // 선택한 날짜를 Intent에 추가합니다.
            startActivity(intent)
        }

        if (true) { //팝업이 뜨는 조건 추가하기
            showCustomDialog()
        }
    }

    private fun showCustomDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.activity_popup, null)

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .create()

        // 팝업창 배경 흐리게 처리
        dialog.window?.setBackgroundDrawable(ContextCompat.getDrawable(this, android.R.color.transparent))

        // 팝업창 닫기 버튼 클릭 이벤트 처리
        dialogView.findViewById<ImageButton>(R.id.popupExitBtn)?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}



