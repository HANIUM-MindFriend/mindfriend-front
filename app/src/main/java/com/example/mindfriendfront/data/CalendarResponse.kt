package com.example.mindfriendfront.data

data class CalendarResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: List<CalendarData>
)
