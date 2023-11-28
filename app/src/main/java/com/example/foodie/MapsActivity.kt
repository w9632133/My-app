package com.example.foodie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.foodie.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
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

        // Add a marker in Sydney and move the camera


// Add a marker for Noma in Copenhagen, Denmark
        val nomaCopenhagen = LatLng(55.7182723, 12.5981207)
        mMap.addMarker(MarkerOptions()
            .position(nomaCopenhagen)
            .title("Noma Copenhagen")
        )

// Add a marker for Geranium in Copenhagen, Denmark
        val geraniumCopenhagen = LatLng(55.6930895, 12.5868817)
        mMap.addMarker(MarkerOptions()
            .position(geraniumCopenhagen)
            .title("Geranium Copenhagen")
        )

// Add a marker for Osteria Francescana in Modena, Italy
        val osteriaFrancescanaModena = LatLng(44.6607104, 10.9065623)
        mMap.addMarker(MarkerOptions()
            .position(osteriaFrancescanaModena)
            .title("Osteria Francescana Modena")
        )

// Add a marker for Mirazur in Menton, France
        val mirazurMenton = LatLng(43.731211, 7.464512)
        mMap.addMarker(MarkerOptions()
            .position(mirazurMenton)
            .title("Mirazur Menton")
        )

// Add a marker for DiverXO in Madrid, Spain
        val diverXoMadrid = LatLng(40.4421145, -3.6783848)
        mMap.addMarker(MarkerOptions()
            .position(diverXoMadrid)
            .title("DiverXO Madrid")
        )

        

    }
}