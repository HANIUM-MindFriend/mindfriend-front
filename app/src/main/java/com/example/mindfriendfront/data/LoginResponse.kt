package com.example.mindfriendfront.data

data class LoginResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: UserData // 여기서 UserData는 로그인 응답 데이터의 내용에 따라 정의될 수 있습니다.
)

