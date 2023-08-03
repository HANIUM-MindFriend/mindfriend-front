package com.example.mindfriendfront

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


class Login : AppCompatActivity() {
    private lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginBtn = findViewById(R.id.loginBtn)
        loginBtn.setOnClickListener {
            Toast.makeText(applicationContext, "로그인 클릭", Toast.LENGTH_SHORT).show()
        }

        val join = findViewById<TextView>(R.id.join)
        join.setOnClickListener {
            Toast.makeText(applicationContext, "회원가입 클릭", Toast.LENGTH_SHORT).show()
        }

        val findId = findViewById<TextView>(R.id.findId)
        findId.setOnClickListener {
            Toast.makeText(applicationContext, "아이디 찾기 클릭", Toast.LENGTH_SHORT).show()
        }

        val findPw = findViewById<TextView>(R.id.findPw)
        findPw.setOnClickListener {
            Toast.makeText(applicationContext, "아이디 찾기 클릭", Toast.LENGTH_SHORT).show()
        }
    }
}