package com.example.mindfriendfront.data

data class DiaryGetResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: DiaryData
)
