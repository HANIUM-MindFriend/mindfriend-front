package com.example.mindfriendfront.data



data class DashboardResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: DashboardData
)