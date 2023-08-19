package com.example.mindfriendfront

import android.R.layout
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class DayInfo : AppCompatActivity() {
    private lateinit var btnBack: ImageButton
    private lateinit var colorView: View
    companion object {
        private const val REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1
        fun newInstance(): DayInfo {
            return DayInfo()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_info)

        btnBack = findViewById(R.id.btnBack)
        colorView = findViewById(R.id.colorView)

        btnBack.setOnClickListener {
//            Toast.makeText(applicationContext, "버튼 클릭", Toast.LENGTH_SHORT).show()
            changeViewColor(colorView, "value1")
        }
    }
    fun changeViewColor(view: View, data: String) {
        val color: Int = when (data) {
            "value1" -> Color.RED
            "value2" -> Color.GREEN
            "value3" -> Color.BLUE
            else -> Color.BLACK
        }
        view.setBackgroundColor(color)
    }
}