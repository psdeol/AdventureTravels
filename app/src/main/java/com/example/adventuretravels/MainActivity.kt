package com.example.adventuretravels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    var currentItemID = R.id.activities

    companion object {
        var VACATIONLIST: MutableList<VacationData> = mutableListOf()
        var ACTIVITYLIST: MutableList<ActivityData> = mutableListOf()
        val flightsFragment = FlightsFragment()
        val activitiesFragment = ActivitiesFragment()
        val hotelsFragment = HotelsFragment()
        val vacationsFragment = VacationsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.activities
        setCurrentFragment(activitiesFragment)
        var db = DatabaseManager(this)

        var readRows = db.readallrows()
        VACATIONLIST = readRows


        vacationsFragment.list = VACATIONLIST
        vacationsFragment.listAdapter = VacationDataRecyclerViewAdapter(VACATIONLIST)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.flights -> {
                    currentItemID = R.id.flights
                    setCurrentFragment(flightsFragment)
                }
                R.id.activities -> {
                    currentItemID = R.id.activities
                    setCurrentFragment(activitiesFragment)
                }
                R.id.hotels -> {
                    currentItemID = R.id.hotels
                    setCurrentFragment(hotelsFragment)
                }
                R.id.vacations -> {
                    currentItemID = R.id.vacations
                    setCurrentFragment(vacationsFragment)
                }
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("CURRENTITEMID", currentItemID)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentItemID = savedInstanceState.getInt("CURRENTITEMID")

        when (currentItemID) {
            R.id.flights -> setCurrentFragment(flightsFragment)
            R.id.activities -> setCurrentFragment(activitiesFragment)
            R.id.hotels -> setCurrentFragment(hotelsFragment)
            R.id.vacations -> setCurrentFragment(vacationsFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.about, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.about) {
            displayInfo(item)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun displayInfo(item: MenuItem) {
        startActivity(Intent(this, AboutActivity::class.java))
    }
}