package com.example.adventuretravels

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DatabaseManager(context: Context): SQLiteOpenHelper(context, "AdventuresDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS VACATIONS(vactionID, location, startDate, endDate)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS EVENTS(eventID, vacationID, location, date, startTime, description)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //no implementation
    }

    fun addVacation(id: String, location: String, startDate: String, endDate: String) {
        writableDatabase.execSQL("INSERT INTO VACATIONS VALUES(\"$id\", \"$location\", \"$startDate\", \"$endDate\")")
    }

    fun addEvent(eID: String, vID: String, location: String, date: String, time: String, description: String) {
        writableDatabase.execSQL("INSERT INTO EVENTS VALUES(\"$eID\", \"$vID\", \"$location\", \"$date\", \"$time\", \"$description\")")
    }


    // Return a list of all rows from the database
    fun readallrows(): MutableList<VacationData> {
        val result = mutableListOf<VacationData>()

        // Get a table back from the query
        val cursor = writableDatabase.rawQuery("SELECT * FROM VACATIONS", null)
        while(cursor.moveToNext()) {

            Log.d("DB", cursor.getString(0))
            result.add(VacationData(cursor.getString(0).toInt() - 1,
                                    cursor.getString(1),
                                    cursor.getString(2),
                                    cursor.getString(3)))
        }
        return result
    }

}