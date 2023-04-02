package com.example.bitfit

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataLlistFragment : Fragment() {

    private val healthDataList = mutableListOf<HealthData>()
    private lateinit var inputBtn: Button
    private lateinit var healthDataAdapter: HealthDataAdapter
    private lateinit var healthDataRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_data_llist, container, false)

        // Add these configurations for the recyclerView and to configure the adapter
        inputBtn = view.findViewById(R.id.inputBtn)
        healthDataRV = view.findViewById(R.id.inputsRV)
        healthDataAdapter =HealthDataAdapter(requireContext(), healthDataList)
        healthDataRV.adapter = healthDataAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        // get all data from database to show
        lifecycleScope.launch {
            (requireActivity().application as BitFitApplication).db.healthDataDao().getAll().collect {
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
            }
            }
        }


        // go to input page when input data button is hit
        inputBtn.setOnClickListener{
            val intent = Intent(requireContext(), InputActivity::class.java)
            startActivity(intent)
        }


        // Set Adapter's onClickListener - to delete items
        healthDataAdapter.setOnItemClickListener(object: HealthDataAdapter.OnItemClickListener{
            override fun onLongPress(position: Int) {
                val date = healthDataList[position].date
                lifecycleScope.launch(Dispatchers.IO) {
                    val data = (requireActivity().application as BitFitApplication).db.healthDataDao().get(date).get(0)
                    (requireActivity().application as BitFitApplication).db.healthDataDao().delete(data)
                }
            }
        }
        )

    }

    companion object {
        fun newInstance(): DataLlistFragment {
            return DataLlistFragment()
        }
    }



}