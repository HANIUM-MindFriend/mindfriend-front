package com.example.mindfriendfront.data

import java.time.LocalDateTime

data class GetColorByMonth (
    val diaryIdx: Long,
    val createdAt: LocalDateTime,
    val mainEmotion: String
)