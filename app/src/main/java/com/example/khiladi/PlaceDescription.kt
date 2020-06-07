package com.example.khiladi


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


class PlaceDescription : Fragment() {

    private lateinit var placeDescriptionBinding: FragmentPlaceDescriptionBinding
    private var selectedAd = Ads2()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var khiladi = Khiladi()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        placeDescriptionBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_place_description, container, false)
        selectedAd = arguments?.getParcelable("selectedAd")!!
        getData()
        placeDescriptionBinding.profilePlaceDescription.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("khiladi",khiladi)
            findNavController().navigate(R.id.action_placeDescription_to_khiladiProfile,bundle)
        }
        placeDescriptionBinding.mapPlaceDescription.setOnClickListener {
            findNavController().navigate(R.id.action_placeDescription_to_customizeMyPlaces)
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
        placeDescriptionBinding.imagesliderPlaceDescription.setItems(list)
        Picasso.get().load(selectedAd.mapSnapshot).into(placeDescriptionBinding.mapPlaceDescription)
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



}
