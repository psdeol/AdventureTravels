package com.example.adventuretravels

import java.io.Serializable

class ActivityData(val activityNum: Int, val name: String, val date: String, val time: String, val description: String) : Serializable {
//val name: String?, val description: String?, val rating: String?, val lat: Double?, val long: Double?
    val content: String = "$name: $date, $time"
}