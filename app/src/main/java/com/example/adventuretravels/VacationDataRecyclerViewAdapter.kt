
package com.example.adventuretravels

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class VacationDataRecyclerViewAdapter(
    private val values: List<VacationData>
) : RecyclerView.Adapter<VacationDataRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_vacations_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.vacationNum.toString()
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val contentView: TextView = view.findViewById(R.id.content)

        init {
            contentView.setOnClickListener {
                val clickedItemId = idView.text.toString()
                val intent = Intent(view.context, VacationDetailsActivity::class.java)
                with (intent) {
                    putExtra("ITEMID", clickedItemId.toInt())
                    view.context.startActivity(this)
                }
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}