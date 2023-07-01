package com.example.mindfriendfront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.CalendarView



class Calendar : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var selectedDateTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        calendarView = findViewById(R.id.calendarView)
        selectedDateTextView = findViewById(R.id.selectedDateTextView)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "${dayOfMonth}/${month + 1}/${year}"
            selectedDateTextView.text = selectedDate
        }

    }

}