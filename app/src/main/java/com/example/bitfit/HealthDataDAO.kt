package com.example.bitfit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthDataDAO {

    @Query("SELECT * FROM health_data_table")
    fun getAll(): Flow<List<HealthDataEntity>>

    @Insert
    fun insert(healthData: HealthDataEntity)

    @Delete
    fun delete(healthData: HealthDataEntity)

    @Query("DELETE FROM health_data_table")
    fun deleteAll()

    @Query("SELECT * FROM health_data_table WHERE id = :idNo ")
    fun get(idNo: Long): HealthDataEntity

    @Query("SELECT AVERAGE(:col) FROM health_data_table")
    fun getAverage(col: String): Double

}