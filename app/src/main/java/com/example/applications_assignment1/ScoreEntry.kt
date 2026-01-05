package com.example.applications_assignment1

data class ScoreEntry(
    val score: Int,
    val lat: Double,
    val lon: Double,
    val timestamp: Long = System.currentTimeMillis()
)
