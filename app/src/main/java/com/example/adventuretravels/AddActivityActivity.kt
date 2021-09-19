package com.example.adventuretravels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker

class AddActivityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        val editName: EditText = findViewById(R.id.editLocation)
        val dateStart: DatePicker = findViewById(R.id.dateStart)
        val timeStart: TimePicker = findViewById(R.id.startTime)
        val editDescription: EditText = findViewById(R.id.editDescription)

        val buttonAddEvent: Button = findViewById(R.id.buttonFinish)

        buttonAddEvent.setOnClickListener {
            val name = editName.text.toString()
            val start = "${dateStart.year}-${dateStart.month}-${dateStart.dayOfMonth}"
            val time = "${timeStart.currentHour}:${timeStart.currentMinute}"
            val description = editDescription.text.toString()
            val ret = Intent()
            ret.putExtra("name", name)
            ret.putExtra("start", start)
            ret.putExtra("time", time)
            ret.putExtra("description", description)
            setResult(RESULT_OK, ret)
            finish() //is this right?
        }
    }
}