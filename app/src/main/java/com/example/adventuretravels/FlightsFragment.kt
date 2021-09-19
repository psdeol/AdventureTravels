package com.example.adventuretravels

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amadeus.android.Amadeus
import com.amadeus.android.ApiResult
import com.amadeus.android.domain.resources.FlightOfferSearch
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class FlightsFragment : Fragment(), FlightOfferSearchRecyclerViewAdapter.ItemClickListener {

    var flightsList: List<FlightOfferSearch>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_flights, container, false)

        val amadeus = Amadeus.Builder(requireContext())
            .setClientId("GFpKF7Zv9NQ2OCHTHz5J54tUnGGahnwd") // API Key
            .setClientSecret("Icx4U9PWDU8oFk6R") // API Secret
            .build()

        val sourceInput: EditText = view.findViewById(R.id.edit_text_source)
        val destinationInput: EditText = view.findViewById(R.id.edit_text_destination)
        val searchButton: Button = view.findViewById(R.id.button_search_flights)
        val newSearchButton: Button = view.findViewById(R.id.button_new_search_flights)

        val adultsPicker: NumberPicker = view.findViewById(R.id.number_picker_adults)
        val childrenPicker: NumberPicker = view.findViewById(R.id.number_picker_children)
        val infantsPicker: NumberPicker = view.findViewById(R.id.number_picker_infants)

        val departurePicker: DatePicker = view.findViewById(R.id.date_picker_departure)

        val recyclerView: RecyclerView = view.findViewById(R.id.flights_list)
        val inputLayout: LinearLayout = view.findViewById(R.id.linear_layout_input_flights)

        adultsPicker.minValue = 1
        adultsPicker.maxValue = 10
        childrenPicker.minValue = 0
        childrenPicker.maxValue = 10
        infantsPicker.minValue = 0
        infantsPicker.maxValue = 10

        searchButton.setOnClickListener {
            val sourceIATA = sourceInput.text.toString()
            val destinationIATA = destinationInput.text.toString()
            val adults = adultsPicker.value
            val children = childrenPicker.value
            val infants = infantsPicker.value
            val departureDate = getFormattedDate(departurePicker)

            GlobalScope.launch {
                when (val result = amadeus.shopping.flightOffers.get(sourceIATA, destinationIATA, departureDate = departureDate, adults = adults, children = children, infants = infants, currencyCode = "USD")) {
                    is ApiResult.Success -> {
                        flightsList = result.data

                        activity?.runOnUiThread {
                            if (resources.configuration.orientation == 1) {
                                inputLayout.visibility = View.GONE
                                newSearchButton.visibility = View.VISIBLE
                                recyclerView.visibility = View.VISIBLE
                            }

                            recyclerView.layoutManager = LinearLayoutManager(context)
                            recyclerView.adapter = FlightOfferSearchRecyclerViewAdapter(result.data, this@FlightsFragment)
                        }
                    }
                    is ApiResult.Error -> {
                        activity?.runOnUiThread {
                            Toast.makeText(requireContext(), "Invalid item", Toast.LENGTH_SHORT).show()
                        }
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

    override fun showFlightDetails(position: Int) {
        val intent = Intent(context, FlightDetailsActivity::class.java)
        val flight: FlightOfferSearch = flightsList!![position]

        with (intent) {
            putExtra("PRICE", flight.price?.total)
            putExtra("CABIN", flight.travelerPricings!![0].fareDetailsBySegment!![0].cabin)
            putExtra("SEATS", flight.numberOfBookableSeats)
            putExtra("DURATION", flight.itineraries!![0].duration)
            putExtra("SEGMENTS", flight.itineraries!![0].segments?.size)
            putExtra("LASTBOOKINGDATE", flight.lastTicketingDate)
        }

        startActivity(intent)
    }

    private fun getFormattedDate(datePicker: DatePicker) : String {
        val year = datePicker.year.toString()
        var month = ""
        var day = ""

        if ((datePicker.month + 1) < 10)
            month = "0" + (datePicker.month + 1)
        else
            month = (datePicker.month + 1).toString()

        if (datePicker.dayOfMonth < 10)
            day = "0" + datePicker.dayOfMonth
        else
            day = datePicker.dayOfMonth.toString()

        return "$year-$month-$day"
    }

    private fun getLatLngFromAddress(context: Context, strAddress: String) : LatLng? {
        val coder = Geocoder(context)
        var p1: LatLng? = null

        try {
            val address = coder.getFromLocationName(strAddress, 5)
            if (address == null)
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

    /* This doesn't seem to be returning the correct city codes */
    private fun getIATACode(latLng: LatLng) : String {
        val API = "http://iatageo.com/getCode/${latLng.latitude}/${latLng.longitude}"
        val text = URL(API).readText()
        val code = JSONObject(text).getString("IATA")
        return code
    }

    private fun getIATACode2(amadeus: Amadeus, location: String) {
        GlobalScope.launch {
            var cities = listOf<String>()
            val result = amadeus.referenceData.locations.get(subType = cities, keyword = location)
        }
    }
}