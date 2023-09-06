package com.example.mindfriendfront.data

data class DiarySingleResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: DiarySingleData
)
