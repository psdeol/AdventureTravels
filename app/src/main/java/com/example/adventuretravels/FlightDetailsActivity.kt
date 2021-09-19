package com.example.adventuretravels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class FlightDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_details)

        val priceText: TextView = findViewById(R.id.text_price)
        val cabinText: TextView = findViewById(R.id.text_cabin)
        val seatsText: TextView = findViewById(R.id.text_seats)
        val durationText: TextView = findViewById(R.id.text_duration)
        val segmentsText: TextView = findViewById(R.id.text_segments)
        val ticketingText: TextView = findViewById(R.id.text_booking_date)
        val doneButton: Button = findViewById(R.id.button_done)

        priceText.text = "Price: ${intent.getDoubleExtra("PRICE", 0.0)}"
        cabinText.text = "Cabin: ${intent.getStringExtra("CABIN")}"
        seatsText.text = "Number of Bookable Seats: ${intent.getIntExtra("SEATS", 0)}"
        durationText.text = "Duration: ${intent.getStringExtra("DURATION")}"
        segmentsText.text = "Segments: ${intent.getIntExtra("SEGMENTS", 0)}"
        ticketingText.text = "Last Booking Date: ${intent.getStringExtra("LASTBOOKINGDATE")}"

        doneButton.setOnClickListener {
            finish()
        }
    }
}