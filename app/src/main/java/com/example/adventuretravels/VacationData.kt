package com.example.adventuretravels

import java.io.Serializable

class VacationData(val vacationNum: Int, val location: String, val startDate: String, val endDate: String) : Serializable {
    val saveLocation = location
    val saveStartDate = startDate
    val saveEndDate = endDate
    val content: String = "$location $startDate:$endDate"
}