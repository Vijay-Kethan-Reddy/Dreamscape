package com.example.dreamscape.Models

data class DreamPost(
    val id: String = "",
    val userId: String = "",
    val categoryId: String = "",
    val title: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val likes: List<String> = emptyList(),
    val saves: List<String> = emptyList(),
    val commentCount: Int = 0
)
