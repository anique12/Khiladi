package com.example.khiladi.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.khiladi.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.libraries.places.api.Places

/**
 * A simple [Fragment] subclass.
 */
class Purchase : Fragment(),OnMapReadyCallback {

    override fun onMapReady(p0: GoogleMap?) {
        Toast.makeText(context,"sasas",Toast.LENGTH_SHORT).show()
    }

    private lateinit var purchaseView: View
    private lateinit var mapView : MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        purchaseView =  inflater.inflate(R.layout.fragment_purchase, container, false)

      /*  Places.initialize(context!!,getString(R.string.google_maps_key))
        try {
            MapsInitializer.initialize(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
        purchaseView
        mapView = purchaseView.findViewById(R.id.mapView) as MapView
        mapView.getMapAsync(this)
        return purchaseView
    }


}
