package com.example.mindfriendfront

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.mindfriendfront.data.UserLogin
import com.example.mindfriendfront.network.ApiServiceFactory.apiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Login : AppCompatActivity() {
    private lateinit var loginBtn: Button
    private  lateinit var editPassword: EditText
    private  lateinit var editText: EditText
    private fun storeAccessToken(token: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("accessToken", token)
        editor.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        editPassword = findViewById(R.id.editTextTextPassword)
        editText = findViewById(R.id.editTextText)
        loginBtn = findViewById(R.id.loginBtn)
        loginBtn.setOnClickListener {
            //Toast.makeText(applicationContext, "로그인 클릭", Toast.LENGTH_SHORT).show()
            val userId = editPassword.text.toString()
            val userPassword = editText.text.toString()

            val userLoginData = UserLogin(userId, userPassword)
            val call = apiService.loginUser(userLoginData)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // 성공적인 응답 처리
                        Toast.makeText(applicationContext, "로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show()
                        // 로그인 성공
                        val accessToken = response.headers()["Authorization"]
                        if (accessToken != null) {
                            // 성공적으로 토큰을 받았을 때, 저장합니다.
                            storeAccessToken(accessToken)
                        }

                    } else {
                        // 실패한 응답 처리
                        val message = "응답 코드: ${response.code()}, 메시지: ${response.message()}"
                        Log.e("API_RESPONSE", message)
                        Toast.makeText(applicationContext, "로그인에 실패하셨습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // 네트워크 실패 처리
                    Log.e("API_RESPONSE", "네트워크 실패: ${t.message}")
                    Toast.makeText(applicationContext, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            })


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