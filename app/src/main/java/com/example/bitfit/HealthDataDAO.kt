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

}