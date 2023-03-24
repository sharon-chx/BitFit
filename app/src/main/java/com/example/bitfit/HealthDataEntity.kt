package com.example.bitfit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_data_table")
data class HealthDataEntity(

    // date is also the primary key
    @PrimaryKey
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "sleepHours") val sleepHours: Int,
    @ColumnInfo(name = "exerciseHours") val exerciseHours: Int,
    @ColumnInfo(name = "notes") val notes: String?

)
