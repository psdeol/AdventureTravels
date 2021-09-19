package com.example.adventuretravels

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.adventuretravels.databinding.FragmentActivitiesListBinding

class ActivityRecyclerViewAdapter(private val values: List<ActivityData>, val listener: ItemClickListener) : RecyclerView.Adapter<ActivityRecyclerViewAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun showActivityDetails (position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentActivitiesListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.activityNum.toString()
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentActivitiesListBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        init {
            contentView.setOnClickListener {
                listener.showActivityDetails(adapterPosition)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}