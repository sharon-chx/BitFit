package com.example.bitfit

import android.provider.SyncStateContract
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_data_table")
data class HealthDataEntity(

    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    @ColumnInfo(name = "date") val date: String?,

    @ColumnInfo(name = "sleepHours") val sleepHours: Int,

    @ColumnInfo(name = "exerciseHours") val exerciseHours: Int,

    @ColumnInfo(name = "notes") val notes: String?

)
