package com.example.bitfit

import android.app.DatePickerDialog
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*

class InputActivity : AppCompatActivity() {
    lateinit var dateBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        dateBtn = findViewById<Button>(R.id.dateButton)
        val sleepHrInput = findViewById<SeekBar>(R.id.sleepBar)
        val exerciseHrInput = findViewById<SeekBar>(R.id.exerciseBar)
        val notesInput = findViewById<EditText>(R.id.noteInput).text
        val saveBtn = findViewById<Button>(R.id.inputBtn)
        val sleepHrDisplay = findViewById<TextView>(R.id.sleepData)
        val exerciseHrDisplay = findViewById<TextView>(R.id.exerciseData)


        // seekbar changes listener
        sleepHrInput.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                sleepHrDisplay.text = progress.toString() + " hours"
            }
        })

        exerciseHrInput.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                exerciseHrDisplay.text = progress.toString() + " hours"
            }
        })

        // Button Listener for pulling up calendar to select date
        dateBtn.setOnClickListener {
            getDate()
        }

        // Button Listener to save data to database
        saveBtn.setOnClickListener{

            // check if the date is already in datebase
            lifecycleScope.launch(IO) {

                // check if data for the date has existed, only insert to database if not existed
                val checkData = (application as BitFitApplication).db.healthDataDao().get(dateBtn.text.toString())

                if (checkData.isEmpty()) {
                    val newData = HealthData(
                        dateBtn.text.toString(),
                        sleepHrInput.progress,
                        exerciseHrInput.progress,
                        notesInput.toString()
                    )

                    lifecycleScope.launch(IO) {
                        (application as BitFitApplication).db.healthDataDao().insert(newData)
                    }
                } else {

                    // need to use getMainLooper() to Toast on UI thread
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(applicationContext, "You have entered info for this date already!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            closeKeyboard()
            // go back to MainActivity
            finish()
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


    /*
    * function to close keyboard after submitting data
    */
    private fun closeKeyboard(){
        val view = this.currentFocus
        if (view != null){
            val manager: InputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager

            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun insertError(){

    }
}