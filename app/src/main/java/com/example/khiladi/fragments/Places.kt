package com.example.khiladi.fragments


import AdsAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Models.Ads2
import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentPlacesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Places : Fragment(),AdsAdapter.PlaceListener {

    override fun eventSelected(ads2: Ads2) {

    }

    override fun listener(ads2: Ads2) {
        selectedAd = ads2
        val bundle = Bundle()
        bundle.putParcelable("selectedAd",selectedAd)
        findNavController().navigate(R.id.action_places_to_placeDescription,bundle)
    }

    private lateinit var placesBinding: FragmentPlacesBinding
    private var currentUser = FirebaseAuth.getInstance().currentUser?.uid
    private var selectedAd = Ads2()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        placesBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_places, container, false)
        getData()
        return placesBinding.root
    }

    private fun getData() {
            val list = ArrayList<Ads2>()
            FirebaseDatabase.getInstance().getReference("ads").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }
                override fun onDataChange(p0: DataSnapshot) {
                   p0.children.forEach {
                       it.children.forEach {
                           it.children.forEach {
                               it.children.forEach {
                                   it.children.forEach {
                                       it.children.forEach {
                                           val ads = it.getValue(Ads2::class.java)
                                           list.add(ads!!)
                                       }
                                   }
                               }
                           }
                       }
                   }
                    placesBinding.recyclerViewPlaces.adapter = AdsAdapter(context!!,this@Places,list,null)
                }

            })
        }


}

