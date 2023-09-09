package com.example.mindfriendfront.data

data class DayInfoResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: DayInfoData
)
