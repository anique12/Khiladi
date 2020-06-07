package com.example.khiladi.fragments


import AdsAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Models.Ads
import com.example.khiladi.Models.Ads2

import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentMyAdsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 */
class MyAds : Fragment(),AdsAdapter.PlaceListener {

    override fun listener(ads2: Ads2) {
        val bundle = Bundle()
        bundle.putParcelable("selectedAd",ads2)
        findNavController().navigate(R.id.action_myAds_to_placeDescription,bundle)
    }

    override fun eventSelected(ads2: Ads2) {
        Toast.makeText(context,"event selected",Toast.LENGTH_SHORT).show()
    }

    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var firebaseUserId = FirebaseAuth.getInstance().currentUser?.uid
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentMyAdsBinding>(inflater,R.layout.fragment_my_ads,container,false)
        val list = ArrayList<Ads2>()
        firebaseDatabase.getReference("khiladi/${firebaseUserId}/ads").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                    val adRef = it.value.toString()
                    firebaseDatabase.getReference(adRef).addValueEventListener(object :ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                           val ads2 = p0.getValue(Ads2::class.java)
                            list.add(ads2!!)
                            binding.recyclerViewMyAds.adapter = AdsAdapter(context!!,null,list,null,this@MyAds)

                        }
                    })
                }

            }

        })

        return binding.root
    }



}
