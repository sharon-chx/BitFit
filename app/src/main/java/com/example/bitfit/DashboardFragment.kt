package com.example.bitfit

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    lateinit var statBtn: Button

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

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // calculate average and update average button
        calAverage()

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
                    "Average hours of sleep: " + sleepAverage.toString() + " hours \n" +
                            "Average hours of workout: " + exerciseAverage.toString() + " hours"
            }

        }


    }

}
