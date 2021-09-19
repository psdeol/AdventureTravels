package com.example.adventuretravels

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.amadeus.android.domain.resources.HotelOffer

import com.example.adventuretravels.databinding.FragmentHotelsListBinding

class HotelOfferRecyclerViewAdapter(private val values: List<HotelOffer>) : RecyclerView.Adapter<HotelOfferRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentHotelsListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.hotel?.rating.toString()
        holder.contentView.text = item.hotel?.name.toString()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentHotelsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}