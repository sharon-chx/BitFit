package com.example.bitfit

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var dateBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dateBtn = findViewById<Button>(R.id.dateButton)
        val sleepHrInput = findViewById<SeekBar>(R.id.sleepBar).progress
        val exerciseHrInput = findViewById<SeekBar>(R.id.exerciseBar).progress
        val notesInput = findViewById<EditText>(R.id.noteInput)
        val saveBtn = findViewById<Button>(R.id.submitBtn)

        val sleepHrDisplay = findViewById<TextView>(R.id.sleepData)
        sleepHrDisplay.text = sleepHrInput.toString() + " hours"

        val exerciseHrDisplay = findViewById<TextView>(R.id.exerciseData)
        exerciseHrDisplay.text = exerciseHrInput.toString() + " hours"

        dateBtn.setOnClickListener {
            getDate()
        }

        saveBtn.setOnClickListener{
            // save data to database
            (application as BitFitApplication).db.healthDataDao().insert(
                HealthDataEntity(
                    dateBtn.text.toString(),
                    sleepHrInput,
                    exerciseHrInput,
                    notesInput.toString()
                )
            )
        }

    }

    private fun getDate() {
        // the instance of our calendar.
        val c = Calendar.getInstance()
        val cYear = c.get(Calendar.YEAR)
        val cMonth = c.get(Calendar.MONTH)
        val cDay = c.get(Calendar.DAY_OF_MONTH)


        // creating a variable for date picker dialog.
        val datePickerDialog = DatePickerDialog(
            this,
            {
                view, year, monthOfYear, dayOfMonth ->
                // setting date to our text view.
                dateBtn.text =  makeDateString(monthOfYear + 1, dayOfMonth, year)
            },
            cYear,
            cMonth,
            cDay
        )
        // at last we are calling show to display our date picker dialog.
        datePickerDialog.show()

    }



    private fun makeDateString(month: Int, day: Int, year: Int): String {
        return "$month/$day/$year"
    }


}