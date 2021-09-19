package com.example.adventuretravels

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amadeus.android.domain.resources.Activity
import com.example.adventuretravels.databinding.FragmentActivitiesListBinding

class ActivitySearchRecyclerViewAdapter(private val values: List<Activity>, val listener: ItemClickListener) : RecyclerView.Adapter<ActivitySearchRecyclerViewAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun showActivityDetails (position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentActivitiesListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        if (item.rating != null)
            holder.idView.text = item.rating?.substring(0, 4) + "/5"
        else
            holder.idView.text = "N/A"
        holder.contentView.text = item.name
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