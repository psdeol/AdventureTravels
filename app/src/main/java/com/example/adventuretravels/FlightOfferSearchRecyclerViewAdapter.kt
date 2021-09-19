package com.example.adventuretravels

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.amadeus.android.domain.resources.FlightOfferSearch

import com.example.adventuretravels.databinding.FragmentFlightsListBinding

class FlightOfferSearchRecyclerViewAdapter(private val values: List<FlightOfferSearch>, private val listener: ItemClickListener) : RecyclerView.Adapter<FlightOfferSearchRecyclerViewAdapter.ViewHolder>() {
    interface ItemClickListener {
        fun showFlightDetails(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentFlightsListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = "$${item.price?.total}"
        holder.contentView.text = "Seats Available: ${item.numberOfBookableSeats}\nLast Ticketing Date: ${item.lastTicketingDate}\nDuration: ${item.itineraries?.get(0)?.duration}"
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentFlightsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        init {
            contentView.setOnClickListener {
                listener.showFlightDetails(adapterPosition)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}