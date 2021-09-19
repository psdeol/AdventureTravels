package com.example.adventuretravels

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class VacationsFragment : Fragment() {
    var listAdapter: VacationDataRecyclerViewAdapter = VacationDataRecyclerViewAdapter(
        MainActivity.VACATIONLIST
    )
    var list: MutableList<VacationData> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_vacations, container, false)

        val addVacationButton: FloatingActionButton = view.findViewById(R.id.button_add_vacation)
        val recyclerView: RecyclerView = view.findViewById(R.id.vacations_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = listAdapter
        val db = DatabaseManager(requireContext())

        val intent = Intent(activity, AddVacationActivity::class.java)
        val getData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val loc = it.data?.getStringExtra("location")
            val start = it.data?.getStringExtra("start")
            val end = it.data?.getStringExtra("end")
            //if one is not null they all are
            loc?.apply {
                list.add(VacationData(list.size, loc, start!!, end!!))
                GlobalScope.launch {
                    db.addVacation(list.size.toString(), loc, start, end)
                }
                listAdapter.notifyDataSetChanged()
            }
        }

        addVacationButton.setOnClickListener {
            intent.putExtra("V_NUM", list.size)
            getData.launch(intent)
        }

        return view
    }
}