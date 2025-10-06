package com.example.dreamscape.Models


data class Comment(
    val dreamId: String = "",
    val commentId: String = "",
    val userId: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
