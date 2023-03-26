package com.example.bitfit

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {


    private val healthDataList = mutableListOf<HealthData>()
    lateinit var statBtn: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputBtn = findViewById<Button>(R.id.inputBtn)
        statBtn = findViewById<Button>(R.id.average)

        // get the Adapter and update items
        val healthDataRV = findViewById<RecyclerView>(R.id.inputsRV)
        val healthDataAdapter =HealthDataAdapter(this, healthDataList)
        healthDataRV.adapter = healthDataAdapter

        // get all data from database to show
        lifecycleScope.launch {
            (application as BitFitApplication).db.healthDataDao().getAll().collect {
                databaseList -> databaseList.map{
                    entity ->
                    HealthData(
                        entity.date,
                        entity.sleepHours,
                        entity.exerciseHours,
                        entity.notes
                    )
            }.also { mappedList ->
                healthDataList.clear()
                healthDataList.addAll(mappedList)
                healthDataAdapter.notifyDataSetChanged()
                calAverage()
                }
            }
        }

        // go to input page when input data button is hit
        inputBtn.setOnClickListener{
            val intent = Intent(this@MainActivity, InputActivity::class.java)
            startActivity(intent)
        }


        // Set Adapter's onClickListener - to delete items
        healthDataAdapter.setOnItemClickListener(object: HealthDataAdapter.OnItemClickListener{
            override fun onLongPress(position: Int) {
                val date = healthDataList[position].date
                lifecycleScope.launch(IO) {
                    val data = (application as BitFitApplication).db.healthDataDao().get(date).get(0)
                    (application as BitFitApplication).db.healthDataDao().delete(data)
                }
            }
        }

        )

    }


    /*
    Get average of "sleepHours" and "exerciseHours" columns and display
     */
    private fun calAverage() {

        lifecycleScope.launch(IO) {
            var sleepAverage = (application as BitFitApplication).db.healthDataDao().getAverageSleep()
            var exerciseAverage = (application as BitFitApplication).db.healthDataDao().getAverageExercise()

            // need to use getMainLooper() to Toast on UI thread
            Handler(Looper.getMainLooper()).post{
                statBtn.text = "Average hours of sleep: " + ((sleepAverage * 1.0).toInt()/1.0).toString() + " hours \n" +
                        "Average hours of workout: " + ((exerciseAverage * 1.0).toInt()/1.0).toString() + " hours"
            }

        }


    }




}