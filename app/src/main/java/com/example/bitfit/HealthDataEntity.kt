package com.example.bitfit

import android.provider.SyncStateContract
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_data_table")
data class HealthData(


    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "date") val date: String = "1/1/2023",

    @ColumnInfo(name = "sleepHours") val sleepHours: Int,

    @ColumnInfo(name = "exerciseHours") val exerciseHours: Int,

    @ColumnInfo(name = "notes") val notes: String?,

    // autogenerate item has be the last argument
    //@PrimaryKey(autoGenerate = true) val id: Long = 0

)
