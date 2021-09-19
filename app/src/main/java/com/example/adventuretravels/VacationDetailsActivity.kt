package com.example.adventuretravels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class VacationDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacation_details)

        val location: TextView = findViewById(R.id.details_location)
        val startDate: TextView = findViewById(R.id.details_start_date)
        val endDate: TextView = findViewById(R.id.details_end_date)

        var itemId = intent.getIntExtra("ITEMID", 0)
        var vacationData = MainActivity.VACATIONLIST[itemId]

        location.text = "Location: " + vacationData.saveLocation
        startDate.text = "Start Date: " + vacationData.saveStartDate
        endDate.text = "End Date: " + vacationData.saveEndDate



    }
}