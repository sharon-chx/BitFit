package com.example.bitfit

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class DashboardFragment : Fragment() {

    lateinit var statBtn: Button
    lateinit var chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        // Add configurations for average button
        statBtn = view.findViewById<Button>(R.id.average)

        chart = view.findViewById(R.id.chart)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // calculate average and update average button
        calAverage()

        // set up the chart
        lifecycleScope.launch(IO) {
            val values = (requireActivity().application as BitFitApplication).db.healthDataDao().getLast7DayData()
            val sleepEntries: ArrayList<Entry> = ArrayList()
            val exerciseEntries: ArrayList<Entry> = ArrayList()
            var dateVal = 0
            var xValues: ArrayList<String> = ArrayList()

            for ((count, entity) in values.withIndex()){
                // x values has to be sorted in ascending order
                sleepEntries.add(Entry(count.toFloat(), entity.sleepHours.toFloat()))
                exerciseEntries.add(Entry(count.toFloat(), entity.exerciseHours.toFloat()))
                dateVal = entity.dateNum % 10000
                val dateStr = (dateVal / 100).toString() + "/" + (dateVal % 100).toString()
                xValues.add(dateStr)
            }

            val sleepDataSet = LineDataSet(sleepEntries, "Sleep hours")
            sleepDataSet.color = resources.getColor(R.color.teal_200)
            sleepDataSet.lineWidth = 3f
            sleepDataSet.setDrawValues(false)
            val exerciseDataSet = LineDataSet(exerciseEntries, "Exercise hours")
            exerciseDataSet.color = resources.getColor(R.color.purple_200)
            exerciseDataSet.lineWidth = 3f
            // display y value above the dots in graph
            exerciseDataSet.setDrawValues(false)

            // add two data into line graph
            val finalData = ArrayList<LineDataSet>()
            finalData.add(sleepDataSet)
            finalData.add(exerciseDataSet)
            val data = LineData(finalData as List<ILineDataSet>?)
            data.setDrawValues(true)

            chart.data = data
            chart.invalidate()
            chart.setTouchEnabled(true)
            chart.setPinchZoom(true)


            val xAxis = chart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            // need to set the min & max, so that x axis label algin with data
            xAxis.axisMinimum = 0f
            xAxis.axisMaximum = 6f
            // use String array as x axis label
            xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
            chart.axisRight.isEnabled = false
            chart.description.text = "Last 7 days data"
            chart.setNoDataText("No data yet!")
        }

    }

    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

    /*
    Get average of "sleepHours" and "exerciseHours" columns and display
    */
    private fun calAverage() {

        lifecycleScope.launch(Dispatchers.IO) {
            val sleepAverage =
                (requireActivity().application as BitFitApplication).db.healthDataDao()
                    .getAverageSleep()
            val exerciseAverage =
                (requireActivity().application as BitFitApplication).db.healthDataDao()
                    .getAverageExercise()

            // need to use getMainLooper() to Toast on UI thread
            Handler(Looper.getMainLooper()).post {
                statBtn.text =
                    "Average hours of sleep: " + ((sleepAverage*100.0).roundToInt()/100.0).toString() + " hours \n" +
                            "Average hours of workout: " + ((exerciseAverage*100.0).roundToInt()/100.0).toString() + " hours"
            }

        }


    }

}



