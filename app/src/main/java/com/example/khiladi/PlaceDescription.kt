package com.example.khiladi


import WeekDaysAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Models.Ads2
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.databinding.FragmentPlaceDescriptionBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.net.URI
import android.widget.Toast
import android.content.ActivityNotFoundException
import com.example.khiladi.Models.Slot
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

class PlaceDescription : Fragment() {

    private lateinit var placeDescriptionBinding: FragmentPlaceDescriptionBinding
    private var selectedAd = Ads2()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var khiladi = Khiladi()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        placeDescriptionBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_place_description, container, false)
        selectedAd = arguments?.getParcelable("selectedAd")!!
        if(selectedAd.khiladiId != FirebaseAuth.getInstance().currentUser?.uid){
            placeDescriptionBinding.edit.visibility = View.INVISIBLE
        }else{
            placeDescriptionBinding.bookPlace.visibility = View.INVISIBLE
        }
        getData()
        placeDescriptionBinding.profilePlaceDescription.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("khiladi",khiladi)
            findNavController().navigate(R.id.action_placeDescription_to_khiladiProfile,bundle)
        }
        placeDescriptionBinding.mapPlaceDescription.setOnClickListener {
            //findNavController().navigate(R.id.action_placeDescription_to_customizeMyPlaces)
            val uri : Uri = Uri.parse("google.navigation:q=${selectedAd.latitude},${selectedAd.longitude}")
            showMap(uri)
        }
        placeDescriptionBinding.edit.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("place",selectedAd)
            findNavController().navigate(R.id.action_placeDescription_to_editPlace,bundle)
        }
        inflateUI()
        return placeDescriptionBinding.root
    }

    private fun inflateUI() {
        val list = ArrayList<String>()
        placeDescriptionBinding.price.text = selectedAd.price
        placeDescriptionBinding.title.text = selectedAd.title
        placeDescriptionBinding.description.text = selectedAd.description
        selectedAd.images?.values?.forEach {
            list.add(it)
        }
        placeDescriptionBinding.bookPlace.setOnClickListener {
            val gson = Gson()
            val jsonList = gson.toJson(selectedAd.timings)
            val bundle =  Bundle()
            bundle.putString("timingList",jsonList)
            findNavController().navigate(R.id.action_placeDescription_to_bookPlace2,bundle)
        }
        placeDescriptionBinding.imagesliderPlaceDescription.setItems(list)
        Picasso.get().load(selectedAd.mapSnapshot).into(placeDescriptionBinding.mapPlaceDescription)
        val oldTimingList = selectedAd.timings!!
       /* placeDescriptionBinding.mondayTiming.text = oldTimingList[0][0].dayTime
        placeDescriptionBinding.tuesdayTiming.text = oldTimingList[1][0].dayTime
        placeDescriptionBinding.wednesdayTiming.text = oldTimingList[2][0].dayTime
        placeDescriptionBinding.thursdayTiming.text = oldTimingList[3][0].dayTime
        placeDescriptionBinding.fridayTiming.text = oldTimingList[4][0].dayTime
        placeDescriptionBinding.saturdayTiming.text = oldTimingList[5][0].dayTime
        placeDescriptionBinding.sundayTiming.text = oldTimingList[6][0].dayTime*/
        placeDescriptionBinding.recyclerView.adapter = WeekDaysAdapter(oldTimingList,this)
    }

    private fun getData() {
       firebaseDatabase.getReference("khiladi/${selectedAd.khiladiId.toString()}").addValueEventListener(object : ValueEventListener{
           override fun onCancelled(p0: DatabaseError) {

           }
           override fun onDataChange(p0: DataSnapshot) {
               khiladi = p0.getValue(Khiladi::class.java)!!
               Picasso.get().load(khiladi.profilepic).into(placeDescriptionBinding.profilePhotoPlaceDescription)
               placeDescriptionBinding.namePlaceDescription.text = khiladi.name
           }
       })
    }

    fun showMap(geoLocation: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = geoLocation
        }
        if (intent.resolveActivity(context!!.packageManager) != null) {
            startActivity(intent)
        }
    }



}
