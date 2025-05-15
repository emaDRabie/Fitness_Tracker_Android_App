package com.crowcode.fitnessy.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.crowcode.fitnessy.R

class WorkoutAdapter(
    private var items: List<WorkoutMuscleItem>,
    private val onItemClick: (WorkoutMuscleItem) -> Unit
) : RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iconImageView: ImageView = view.findViewById(R.id.imageViewIcon)
        val titleTextView: TextView = view.findViewById(R.id.txtTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = items[position]
        holder.titleTextView.text = workout.title
        holder.iconImageView.setImageResource(workout.iconResId)

        holder.itemView.setOnClickListener {
            onItemClick(workout)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<WorkoutMuscleItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
