package com.example.khiladi.createEvent


import AdsAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.khiladi.Models.Ads2
import com.example.khiladi.Models.Khiladi

import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentCustomizeMyPlacesBinding
import com.example.khiladi.databinding.FragmentNearbyPlacesBinding
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.ads_single_layout.*
import android.Manifest.permission
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.Manifest
import android.location.Location
import android.os.Build
import android.renderscript.Sampler
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.security.AccessController.checkPermission


/**
 * A simple [Fragment] subclass.
 */
class NearbyPlaces : Fragment(), AdsAdapter.PlaceListener {

    override fun eventSelected(ads2: Ads2) {
        selectedAd = ads2
        val bundle = Bundle()
        bundle.putString("team",arguments?.getString("team").toString())
        bundle.putString("sports",arguments?.getString("sports").toString())
        bundle.putString("place",ads2.id)
        bundle.putString("placeRef",ads2.ref)
        findNavController().navigate(R.id.action_nearbyPlaces_to_schedule,bundle)
    }

    override fun listener(ads2: Ads2) {
        selectedAdForDiscription = ads2
        val bundle = Bundle()
        bundle.putParcelable("selectedAd",selectedAdForDiscription)
        findNavController().navigate(R.id.action_nearbyPlaces_to_placeDescription,bundle)
    }


    private lateinit var nearbyPlacesBinding: FragmentNearbyPlacesBinding
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    val currentUserUid  = FirebaseAuth.getInstance().currentUser?.uid
    val distancelist = HashMap<Float,Ads2>()
    var list = ArrayList<Ads2>()
    var keys = ArrayList<Float>()
    var khiladi = Khiladi()
    private var selectedAd = Ads2()
    private var selectedAdForDiscription = Ads2()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        nearbyPlacesBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_nearby_places, container, false)
        getCurrentUser()


        return nearbyPlacesBinding.root
    }

    private fun showNearbyPlacesUsingCoordinates(location1 : Location) {
        val sports = arguments?.getString("sports").toString()
        val team = arguments?.getString("teamId").toString()
        val urgent = arguments?.getBoolean("urgent")
        firebaseDatabase.getReference("ads/$sports/${khiladi.country}").addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                p0.children.forEach {
                    it.children.forEach {
                        it.children.forEach {
                            it.children.forEach {
                                val place = it.getValue(Ads2::class.java)
                                val location2 = Location("")
                                location2.latitude = place!!.latitude!!.toDouble()
                                location2.longitude = place.longitude!!.toDouble()
                                val distance = location2.distanceTo(location1)
                                distancelist.put(distance, place)
                            }
                        }
                    }
                }
                val sortedmap = distancelist.toSortedMap()
                sortedmap.values.forEach {
                    list.add(it)
                }
                Log.i("nearbyPlacesYar", sortedmap.keys.toString())
                nearbyPlacesBinding.recyclerView.adapter = AdsAdapter(context!!,null,list,this@NearbyPlaces,null)

            }

        })
    }

    private fun getCurrentUser() {
        firebaseDatabase.getReference("khiladi/$currentUserUid").addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                khiladi = p0.getValue(Khiladi::class.java)!!
                //getData()
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M ) {
                    checkPermission()
                }

                if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context!!,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                        //Toast.makeText(context,it.toString(),Toast.LENGTH_SHORT).show()
                        showNearbyPlacesUsingCoordinates(it)
                    }
                }
            }

        })
    }

    private fun getData() {
        val sports = arguments?.getString("sports").toString()
        val team = arguments?.getString("teamId").toString()
        val urgent = arguments?.getBoolean("urgent")
        Log.d("nearbyYarPLaces",khiladi.toString())
        Log.d("nearbyYarPLaces",sports)
        firebaseDatabase.getReference("ads/$sports/${khiladi.country}/${khiladi.city}").addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("nearbyYarPLaces",p0.toString())
                p0.children.forEach{
                        it.children.forEach {
                            it.children.forEach{
                                val place = it.getValue(Ads2::class.java)
                                list.add(place!!)
                                nearbyPlacesBinding.recyclerView.adapter = AdsAdapter(context!!,null,list,this@NearbyPlaces)
                            }

                        }
                    }

            }

        })
    }


    fun checkPermission(){
    if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                       ContextCompat.checkSelfPermission(context!!,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ){//Can add more as per requirement

                ActivityCompat.requestPermissions(activity!!,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),
                        123);
    }
}
}
