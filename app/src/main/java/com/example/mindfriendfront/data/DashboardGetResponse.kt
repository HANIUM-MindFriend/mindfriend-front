package com.example.mindfriendfront.data



data class DashboardGetResponse(
//    val status: Int,
//    val code: String,
//    val message: String,
//    val data: EmoGraph
val graphStatistics: GetEmoGraph,
val moodTracker: List<GetColorByMonth>
)