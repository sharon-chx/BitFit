package com.example.bitfit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthDataDAO {

    @Query("SELECT * FROM health_data_table")
    fun getAll(): Flow<List<HealthData>>

    @Insert
    fun insert(healthData: HealthData)

    @Delete
    fun delete(healthData: HealthData)

    @Query("DELETE FROM health_data_table")
    fun deleteAll()

    @Query("SELECT * FROM health_data_table WHERE date = :date ")
    fun get(date: String): List<HealthData>

    @Query("SELECT AVG(sleepHours) FROM health_data_table")
    fun getAverageSleep(): Double

    @Query("SELECT AVG(exerciseHours) FROM health_data_table")
    fun getAverageExercise(): Double

}