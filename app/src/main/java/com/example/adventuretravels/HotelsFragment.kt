package com.example.adventuretravels

import android.content.Context
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
import com.amadeus.android.domain.resources.HotelOffer
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import java.io.IOException
import java.util.Calendar

lateinit var locationInput: EditText
lateinit var startDate: DatePicker
lateinit var endDate: DatePicker
lateinit var numAdults: NumberPicker

class HotelsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_hotels, container, false)

        val job = SupervisorJob()
        val scope = CoroutineScope(Dispatchers.Main + job)
        val amadeus = Amadeus.Builder(requireContext())
            .setClientId("API KEY") // API Key
            .setClientSecret("API SECRET") // API Secret
            .build()

        locationInput = view.findViewById(R.id.edit_text_location_hotel)
        startDate = view.findViewById(R.id.date_picker_start_date_hotel)
        endDate = view.findViewById(R.id.date_picker_end_date_hotel)
        numAdults = view.findViewById(R.id.number_picker_number_adults_hotel)
        val searchButton: Button = view.findViewById(R.id.button_search_hotels)
        val recyclerView: RecyclerView = view.findViewById(R.id.list_hotels)

        var latLng: LatLng?
        var hotelsList: List<HotelOffer>?

        val checkInCalendar = Calendar.getInstance()
        var checkInDate = ""
        var startDay = checkInCalendar.get(Calendar.DAY_OF_MONTH)
        var startMonth = checkInCalendar.get(Calendar.MONTH)
        var startYear = checkInCalendar.get(Calendar.YEAR)

        //Update the start date
        startDate.init(checkInCalendar.get(Calendar.YEAR), checkInCalendar.get(Calendar.MONTH), checkInCalendar.get(Calendar.DAY_OF_MONTH)) {
                view, year, month, day ->
            startDay = day
            startMonth = month + 1
            startYear = year
            checkInDate = getFormattedDate(startDay, startMonth, startYear)
        }

        var checkOutDate = ""
        var endDay = checkInCalendar.get(Calendar.DAY_OF_MONTH)
        var endMonth = checkInCalendar.get(Calendar.MONTH)
        var endYear = checkInCalendar.get(Calendar.YEAR)

        //Update the end date
        endDate.init(checkInCalendar.get(Calendar.YEAR), checkInCalendar.get(Calendar.MONTH), checkInCalendar.get(Calendar.DAY_OF_MONTH)) {
                view, year, month, day ->
            endDay = day
            endMonth = month + 1
            endYear = year
            checkOutDate = getFormattedDate(endDay, endMonth, endYear)
        }

        numAdults.minValue = 1
        numAdults.maxValue = 10
        numAdults.value = 1

        searchButton.setOnClickListener {
            GlobalScope.launch {
                when (val result = amadeus.shopping.hotelOffers.get(
                    cityCode = locationInput.text.toString(),
                    checkInDate = checkInDate,
                    checkOutDate = checkOutDate,
                    adults = numAdults.value,
                    radius = 5
                )) {
                    is ApiResult.Success -> {
                        hotelsList = result.data
                        activity?.runOnUiThread {
                            recyclerView.layoutManager = LinearLayoutManager(context)
                            recyclerView.adapter = HotelOfferRecyclerViewAdapter(result.data)

                        }
                    }
                    is ApiResult.Error -> {
                        activity?.runOnUiThread {
                            Toast.makeText(requireContext(), "Item not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }

        return view
    }

    private fun getLatLngFromAddress(context: Context, strAddress: String) : LatLng? {
        val coder = Geocoder(context)
        var p1: LatLng? = null

        try {
            val address = coder.getFromLocationName(strAddress, 5)
            if (address == null)
                return null

            val location: Address = address.get(0)
            p1 = LatLng(location.latitude, location.longitude)

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return p1
    }

    private fun getFormattedDate(day: Int, month: Int, year: Int) : String {

        var newMonth = ""
        var newDay = ""

        if (month < 10) {
            newMonth = "0$month"
        } else {
            newMonth = "$month"
        }
        if (day < 10) {
            newDay = "0$day"
        } else {
            newDay = "$day"
        }

        return "$year-$newMonth-$newDay"
    }
}
