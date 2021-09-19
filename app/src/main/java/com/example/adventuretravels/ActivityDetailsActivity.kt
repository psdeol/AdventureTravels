package com.example.adventuretravels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.adventuretravels.databinding.ActivityActivityDetailsBinding
import java.io.Serializable

class ActivityDetailsActivity : AppCompatActivity(), Serializable, OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val activityData = intent.getSerializableExtra("Activity") as ActivitySearchData

        val textName: TextView = findViewById(R.id.text_view_name)
        val textRating: TextView = findViewById(R.id.text_view_rating)
        val textDescription: TextView = findViewById(R.id.text_view_description)
        var location: LatLng? = null
        //TODO reintegrate later
        if (activityData.lat != null && activityData.long != null)
            location = LatLng(activityData.lat, activityData.long)

        textName.text = activityData.name
        textRating.text = "Rating: " + activityData.rating?.substring(0, 4) + "/5"
        textDescription.text = activityData.description

        if (location != null) {
            mMap.addMarker(MarkerOptions().position(location).title("Activity"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15F))
        }
    }
}