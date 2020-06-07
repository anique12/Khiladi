package com.example.khiladi.createPlaceAd


import android.os.Bundle

import com.google.android.gms.location.*
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.khiladi.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.MapsInitializer
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Utils
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_place_location.view.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*


class PlaceLocation : Fragment(),OnMapReadyCallback,PlaceSelectionListener {

    override fun onPlaceSelected(p0: Place) {
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(p0.latLng,15.0f))
        enable()
    }

    override fun onError(p0: Status) {
        Toast.makeText(context,p0.statusMessage,Toast.LENGTH_SHORT).show()
    }

    private var mMap: GoogleMap? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mapView : MapView
    private lateinit var placeAutoCompleteFragment: AutocompleteSupportFragment
    private var onCameraIdleListener: GoogleMap.OnCameraIdleListener? = null
    private var currentCircle : Circle? = null
    private var locality : String? = null
    private var country : String? = null
    private var city : String? = null
    private var coordinates : LatLng? = null
    private var postalCode : String? = null
    private lateinit var locationRequest:LocationRequest
    private lateinit var mView : View
    private var adressOk = true
    val storage = FirebaseStorage.getInstance()
    private var mapSnapShot : String? = null
    private lateinit var utils : Utils
    var bundle = Bundle()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.activity_place_location,container,false)
        utils = Utils(context!!)
            mapView = mView.findViewById(R.id.map) as MapView
            Places.initialize(context!!,getString(R.string.google_maps_key))
        placeAutoCompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment )as AutocompleteSupportFragment
        placeAutoCompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG))
        placeAutoCompleteFragment.setOnPlaceSelectedListener(this)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        initializeAll()
        mView.nextButton.setOnClickListener {
            if(adressOk){
                utils.showLoading()
                getSnapShot()
            }else{
                Toast.makeText(context,"Please Select appropriate location",Toast.LENGTH_SHORT).show()
            }
        }
        return mView.rootView
    }


    private fun getSnapShot() {
        val callback = GoogleMap.SnapshotReadyCallback{
            Log.d("snapshotReady","snapShotReady")
            val baos = ByteArrayOutputStream()
            val filename = UUID.randomUUID().toString()
            it.compress(Bitmap.CompressFormat.JPEG, 25, baos)
            val ref = storage.getReference("ads/$filename")
            ref.putBytes(baos.toByteArray()).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    utils.hideLoading()
                    Log.d("mapUploaded",it.toString())
                    bundle.putString("locality",locality.toString())
                    bundle.putString("country",country?.toLowerCase())
                    bundle.putString("latitude",coordinates?.latitude.toString())
                    bundle.putString("longitude",coordinates?.longitude.toString())
                    bundle.putString("postalCode",postalCode.toString())
                    bundle.putString("city",city.toString())
                    bundle.putString("map",it.toString())
                    findNavController().navigate(R.id.action_placeLocation_to_customGallery,bundle)
                }.addOnFailureListener{
                    Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener{
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
            }
        }
        mMap?.snapshot(callback)
    }

    fun initializeAll(){
        try {
            MapsInitializer.initialize(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mapView = mView.findViewById(R.id.map) as MapView
        mapView.getMapAsync(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)
        configureCameraIdle()
        buildLocationRequest()
        buildLocationCallback()
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }

    private fun configureCameraIdle() {
        onCameraIdleListener = GoogleMap.OnCameraIdleListener {
            val latLng = mMap?.cameraPosition?.target
            val geocoder = Geocoder(context)

            try {
                val addressList = geocoder.getFromLocation(latLng!!.latitude, latLng.longitude, 1)
                if (addressList != null && addressList.size > 0) {
                     locality = addressList[0].getAddressLine(0)
                     country = addressList[0].countryName
                     postalCode = addressList[0].postalCode
                     val adminArea : String? = addressList[0].adminArea
                    city = addressList[0].subAdminArea

                    if (addressList[0].locality != null && addressList[0].countryName != null && addressList[0].adminArea != null && addressList[0].subAdminArea != null) {
                        coordinates = LatLng(latLng.latitude, latLng.longitude)
                        adressOk = true
                        enable()
                        if(currentCircle!=null) currentCircle?.remove()
                        currentCircle = mMap?.addCircle(CircleOptions().center(coordinates).radius(200.0).strokeColor(Color.BLUE).fillColor(android.R.color.transparent).strokeWidth(2.0f))
                    }
                    else{
                        adressOk = false
                        disable()
                    }
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private fun buildLocationCallback() {

         fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                coordinates = LatLng(it.latitude,it.longitude)
                mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15.0f))
                enable()
            }
        }
    }

    private fun enable() {
        mView.nextButton.setTextColor(resources.getColor(android.R.color.white))
        mView.nextButton.setBackgroundColor(resources.getColor(android.R.color.holo_purple))
        mView.nextButton.isEnabled = true
    }


    private fun disable(){
        mView.nextButton.setTextColor(resources.getColor(android.R.color.darker_gray))
        mView.nextButton.background = ContextCompat.getDrawable(context!!,R.drawable.simple_stroke)
        mView.nextButton.isEnabled = false
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val markerOptions =  MarkerOptions()
       markerOptions.position(mMap?.cameraPosition?.target!!)
       val markerCenter = mMap?.addMarker(markerOptions);

       mMap?.setOnCameraMoveListener {
           markerCenter?.setPosition(mMap?.getCameraPosition()?.target!!)
       }
        mMap?.isMyLocationEnabled = true
        mMap?.setOnCameraIdleListener(onCameraIdleListener)
        buildLocationRequest()
        buildLocationCallback()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }


    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}
