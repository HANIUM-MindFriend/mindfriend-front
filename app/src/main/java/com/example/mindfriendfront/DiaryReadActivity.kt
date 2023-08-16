package com.example.mindfriendfront

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date

class DiaryReadActivity :AppCompatActivity () {
    private lateinit var pdfButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val dateTextView: TextView = findViewById(R.id.date)

        val currentDate = Date()

        val dateFormat = SimpleDateFormat("MMdd")
        val formattedDate = dateFormat.format(currentDate)

        dateTextView.text = formattedDate
    }
}