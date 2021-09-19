
package com.example.adventuretravels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddVacationActivity : AppCompatActivity(), ActivityRecyclerViewAdapter.ItemClickListener {
    var listAdapter: ActivityRecyclerViewAdapter = ActivityRecyclerViewAdapter(
        MainActivity.ACTIVITYLIST,
        this
    )
    var list: MutableList<ActivityData> = MainActivity.ACTIVITYLIST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vacation)
        val vNum = intent.getIntExtra("V_NUM", -1)
        list.removeAll {
            true
        }

        val viewLocation: EditText = findViewById(R.id.editLocation)
        val viewDateStart: DatePicker = findViewById(R.id.dateStart)
        val viewDateEnd: DatePicker = findViewById(R.id.dateEnd)

        val addVacationButton: Button = findViewById(R.id.buttonAddVacation)
        val addEventButton: Button = findViewById(R.id.buttonAddEvent)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerEvents)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = listAdapter

        val db = DatabaseManager(this)

        val intent = Intent(this, AddActivityActivity::class.java)
        val getData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val name = it.data?.getStringExtra("name")
            val start = it.data?.getStringExtra("start")
            val time = it.data?.getStringExtra("time")
            val description = it.data?.getStringExtra("description")
            //if one is not null they all are
            name?.apply {
                list.add(ActivityData(list.size, name, start!!, time!!, description!!))
                GlobalScope.launch {
                    db.addEvent(list.size.toString(), vNum.toString(), name, start, time, description)
                }
                listAdapter.notifyDataSetChanged()
            }
        }

        addEventButton.setOnClickListener {
            getData.launch(intent)
        }

        addVacationButton.setOnClickListener {
            val location = viewLocation.text.toString()
            val start = "${viewDateStart.year}-${viewDateStart.month+1}-${viewDateStart.dayOfMonth}"
            val end = "${viewDateEnd.year}-${viewDateEnd.month+1}-${viewDateEnd.dayOfMonth}"
            val ret = Intent()
            ret.putExtra("location", location)
            ret.putExtra("start", start)
            ret.putExtra("end", end)
            setResult(RESULT_OK, ret)
            finish()
        }
    }

    override fun showActivityDetails(position: Int) {

    }
}