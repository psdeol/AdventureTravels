package com.example.adventuretravels

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amadeus.android.Amadeus
import com.amadeus.android.ApiResult
import com.amadeus.android.domain.resources.Activity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import java.io.IOException

class ActivitiesFragment : Fragment(), ActivitySearchRecyclerViewAdapter.ItemClickListener {

    var activitiesList: List<Activity>? = null
    var recentLists = mutableListOf<List<Activity>>()
    var recentCities  = mutableListOf<String>()
    var addRecentIndex = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_activities, container, false)

        val amadeus = Amadeus.Builder(requireContext())
            .setClientId("API KEY") // API Key
            .setClientSecret("API SECRET") // API Secret
            .build()

        val locationInput: EditText = view.findViewById(R.id.edit_text_location)
        val searchButton: Button = view.findViewById(R.id.button_search_activities)
        val newSearchButton: Button = view.findViewById(R.id.button_new_search_activities)
        val recyclerView: RecyclerView = view.findViewById(R.id.activities_list)
        val inputLayout: LinearLayout = view.findViewById(R.id.linear_layout_input_activities)

        var latLng: LatLng?

        searchButton.setOnClickListener {
            var recentlySearched = false
            var recentlySearchedCity: String? = null
            var recentlySearchedIndex = 0

            if (recentCities.size > 0)
                for (i in 0 until recentCities.size) {
                    if (locationInput.text.toString() == recentCities[i]) {
                        recentlySearched = true
                        recentlySearchedIndex = i
                        break
                    }
                }

            if (recentlySearched) {
                    if (resources.configuration.orientation == 1) {
                        inputLayout.visibility = View.GONE
                        newSearchButton.visibility = View.VISIBLE
                        recyclerView.visibility = View.VISIBLE
                    }
                activitiesList = recentLists[recentlySearchedIndex]
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = ActivitySearchRecyclerViewAdapter(recentLists[recentlySearchedIndex], this@ActivitiesFragment)

            } else {
                latLng = getLatLngFromAddress(requireContext(), locationInput.text.toString())

                if (latLng != null) {
                    GlobalScope.launch {
                        when (val result = amadeus.shopping.activities.get(latLng!!.latitude, latLng!!.longitude, 5)) {
                            is ApiResult.Success -> {

                                activitiesList = result.data
                                activity?.runOnUiThread {
                                    if (resources.configuration.orientation == 1) {
                                        inputLayout.visibility = View.GONE
                                        newSearchButton.visibility = View.VISIBLE
                                        recyclerView.visibility = View.VISIBLE
                                    }

                                    recyclerView.layoutManager = LinearLayoutManager(context)
                                    recyclerView.adapter = ActivitySearchRecyclerViewAdapter(result.data, this@ActivitiesFragment)
                                }

                                if (recentCities.size < 10) {
                                    recentCities.add(locationInput.text.toString())
                                    recentLists.add(activitiesList!!)
                                } else {
                                    recentCities[addRecentIndex] = locationInput.text.toString()
                                    recentLists[addRecentIndex] = activitiesList!!
                                    if (addRecentIndex < 4) {
                                        addRecentIndex++
                                    } else {
                                        addRecentIndex = 0
                                    }
                                }
                            }
                        }

                    }
                } else {
                    activity?.runOnUiThread {
                        Toast.makeText(requireContext(), "invalid name", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        newSearchButton.setOnClickListener {
            newSearchButton.visibility = View.GONE
            recyclerView.visibility = View.GONE
            inputLayout.visibility = View.VISIBLE
        }

        return view
    }

    override fun showActivityDetails(position: Int) {
        //TODO reintegrate later
        val intent = Intent(activity, ActivityDetailsActivity::class.java)

        val activity = activitiesList!![position]
        val activityData = ActivitySearchData(activity.name, activity.shortDescription, activity.rating, activity.geoCode?.latitude, activity.geoCode?.longitude)

        intent.putExtra("Activity", activityData)
        startActivity(intent)
    }

    private fun getLatLngFromAddress(context: Context, strAddress: String) : LatLng? {
        val coder = Geocoder(context)
        var p1: LatLng? = null

        try {
            val address = coder.getFromLocationName(strAddress, 5)
            if (address.size == 0)
                return null
            else {
                if (address.size != 0) {
                    val location: Address = address.get(0)
                    p1 = LatLng(location.latitude, location.longitude)
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return p1
    }
}
