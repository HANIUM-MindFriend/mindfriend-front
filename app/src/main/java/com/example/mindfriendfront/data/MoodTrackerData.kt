package com.example.mindfriendfront.data

import java.util.Date


data class MoodTrackerData (
    val diaryIdx: Long,
    val createdAt: Date,
    val mainEmotion: String
)