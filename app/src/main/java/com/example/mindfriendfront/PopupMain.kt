package com.example.mindfriendfront

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PopupMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_main)

        val popupBtn: Button = findViewById(R.id.popupBtn)
        // 버튼 클릭 이벤트 처리
        popupBtn.setOnClickListener {
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