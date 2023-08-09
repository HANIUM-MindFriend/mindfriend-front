package com.example.mindfriendfront.data

data class DuplicateResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: Boolean
)
