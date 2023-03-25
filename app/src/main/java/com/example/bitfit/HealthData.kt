package com.example.bitfit

data class HealthData(
    val id: Long,
    val date: String?,
    val sleepHours: Int,
    val exerciseHours: Int,
    val notes: String?,
): java.io.Serializable
