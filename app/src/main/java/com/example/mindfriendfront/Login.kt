package com.example.mindfriendfront

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.mindfriendfront.data.LoginResponse
import com.example.mindfriendfront.data.UserLogin
import com.example.mindfriendfront.network.ApiServiceFactory.apiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Login : AppCompatActivity() {
    private lateinit var loginBtn: Button
    private  lateinit var editPassword: EditText
    private  lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        editPassword = findViewById(R.id.editTextTextPassword)
        editText = findViewById(R.id.editTextText)
        loginBtn = findViewById(R.id.loginBtn)
        loginBtn.setOnClickListener {
            val userId = editText.text.toString()
            val userPassword = editPassword.text.toString()

            val userLoginData = UserLogin(userId, userPassword)
            val call = apiService.loginUser(userLoginData)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null) {
                            // 로그인 성공 시 서버에서 반환된 토큰 저장
                            val accessToken = loginResponse.data.accessToken
                            saveTokenToSharedPreferences(accessToken)
                            Toast.makeText(applicationContext, "로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show()

                            // Calandar Activity로 이동
                            val intent = Intent(applicationContext, com.example.mindfriendfront.NaviActivity::class.java)
                            startActivity(intent)

                        }
                    } else {
                        // 실패한 응답 처리
                        // 실패한 응답 처리
                            val errorBody = response.errorBody()?.string()
                            val message = "응답 코드: ${response.code()}, 메시지: ${response.message()}, 오류 내용: $errorBody"
                            Log.e("API_RESPONSE", message)
                        Toast.makeText(applicationContext, "로그인에 실패하셨습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // 네트워크 실패 처리
                    Log.e("API_RESPONSE", "네트워크 실패: ${t.message}")
                    Toast.makeText(applicationContext, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            })
        }


        val join = findViewById<TextView>(R.id.join)
        join.setOnClickListener {
            Toast.makeText(applicationContext, "회원가입 클릭", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
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
    private fun saveTokenToSharedPreferences(accessToken: String) {
        val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("accessToken", accessToken)
        editor.apply()
    }

}