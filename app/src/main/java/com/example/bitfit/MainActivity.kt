package com.example.bitfit

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
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

    lateinit var dateBtn: Button
    private val healthData = mutableListOf<HealthData>()
    lateinit var statBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputGroup = findViewById<Group>(R.id.inputGroup)
        dateBtn = findViewById<Button>(R.id.dateButton)
        var sleepHrInput = findViewById<SeekBar>(R.id.sleepBar)
        var exerciseHrInput = findViewById<SeekBar>(R.id.exerciseBar)
        var notesInput = findViewById<EditText>(R.id.noteInput).text
        val saveBtn = findViewById<Button>(R.id.submitBtn)
        val sleepHrDisplay = findViewById<TextView>(R.id.sleepData)
        val exerciseHrDisplay = findViewById<TextView>(R.id.exerciseData)
        statBtn = findViewById<Button>(R.id.average)

        // get the Adapter and update items
        val healthDataRV = findViewById<RecyclerView>(R.id.inputsRV)
        val healthDataAdapter =HealthDataAdapter(this, healthData)
        healthDataRV.adapter = healthDataAdapter

        // seekbar changes listener
        sleepHrInput.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                sleepHrDisplay.text = progress.toString() + " hours"
            }
        })

        exerciseHrInput.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
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

            if (inputGroup.visibility == View.VISIBLE){

                val newData = HealthDataEntity(
                    0,
                    dateBtn.text.toString(),
                    sleepHrInput.progress,
                    exerciseHrInput.progress,
                    notesInput.toString()
                )

                lifecycleScope.launch(IO) {
                    (application as BitFitApplication).db.healthDataDao().insert(newData)
                }

                val newDataClass = HealthData(newData.id, newData.date, newData.sleepHours, newData.exerciseHours, newData.notes)
                healthData.add(newDataClass)

                // notify the changes on adapter
                // get last added item index
                val index = healthDataAdapter.getItemCount()
                // notify the adapter of the insertion of new data
                healthDataAdapter.notifyItemInserted(index)

                // close keyboard, clear input fields and make the input group invisible
                sleepHrInput.setProgress(0)
                exerciseHrInput.setProgress(0)
                notesInput.clear()
                saveBtn.text = "Input data!"
                inputGroup.visibility = View.GONE
                closeKeyboard()

                // recalculate average
                calAverage()
            }
            else{
                inputGroup.visibility = View.VISIBLE
            }
        }

        // Set Adapter's onClickListener
        healthDataAdapter.setOnItemClickListener(object: HealthDataAdapter.OnItemClickListener{
            override fun onLongPress(itemView: View?, position: Int) {
                val id = healthData[position].id
                lifecycleScope.launch(IO) {
                    val data = (application as BitFitApplication).db.healthDataDao().get(id)
                    (application as BitFitApplication).db.healthDataDao().delete(data)
                }
                healthData.removeAt(position)
                healthDataAdapter.notifyItemInserted(position)
            }
        }

        )

    }


    /*
    Get average of "sleepHours" and "exerciseHours" columns and display
     */
    private fun calAverage() {
        var sleepAverage = 0.0
        var exerciseAverage = 0.0
        lifecycleScope.launch {
            sleepAverage = (application as BitFitApplication).db.healthDataDao().getAverage("sleepHours")
            exerciseAverage = (application as BitFitApplication).db.healthDataDao().getAverage("exerciseHours")
        }
        statBtn.text = "Average hours of sleep: " + ((sleepAverage * 1.0).toInt()/1.0).toString() + " hours \n" +
                "Average hours of workout: " + ((exerciseAverage * 1.0).toInt()/1.0).toString() + " hours"

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


}