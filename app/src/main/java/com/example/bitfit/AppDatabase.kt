package com.example.bitfit

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HealthData::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun healthDataDao(): HealthDataDAO

    companion object{

        @Volatile
        private var INSTANCE: AppDatabase? = null

        // build the database when calling getInstance
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it}
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "HealthData-db"
            ).build()

    }

}
