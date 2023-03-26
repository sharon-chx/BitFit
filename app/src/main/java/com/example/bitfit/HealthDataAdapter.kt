package com.example.bitfit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class HealthDataAdapter(private val context: Context, private val healthData: List<HealthData>):
    RecyclerView.Adapter<HealthDataAdapter.ViewHolder>() {

    /***** Creating OnItemClickListener *****/

    // Define the listener interface
    interface OnItemClickListener {
        fun onLongPress(position: Int)
    }

    // Define listener member variable
    private lateinit var listener: OnItemClickListener

    // Define the method that allows the parent activity or fragment to define the listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        // View holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val dateTV = itemView.findViewById<TextView>(R.id.date)
        val sleepTV = itemView.findViewById<TextView>(R.id.sleepHrs)
        val sleepRating = itemView.findViewById<RatingBar>(R.id.sleepRating)
        val exerciseTV = itemView.findViewById<TextView>(R.id.exerciseHrs)
        val exerciseRating = itemView.findViewById<RatingBar>(R.id.exerciseRating)
        val notesTV = itemView.findViewById<TextView>(R.id.noteTV)

        init{
            // Setup the long press listener
            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onLongPress(position)
                }
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.input_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val data = healthData[position]
        // Set item views based on your views and data model
        holder.dateTV.text = data.date
        holder.sleepTV.text = "Slept " + data.sleepHours.toString() + " hrs"
        holder.sleepRating.progress = data.sleepHours
        holder.exerciseTV.text = "Worked out " + data.exerciseHours.toString() + " hrs"
        holder.exerciseRating.progress = data.exerciseHours
        holder.notesTV.text = "Notes: " + data.notes
    }


    override fun getItemCount(): Int {
        return healthData.size
    }


}