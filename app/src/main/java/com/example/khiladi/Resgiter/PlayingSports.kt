package com.example.khiladi.Resgiter


import SelectSportsCategoriesAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.khiladi.Models.SportsCatergory

import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentPlayingSportsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList

class PlayingSports : Fragment(),SelectSportsCategoriesAdapter.SelectedSportsCategoryListener {

    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var categoryList = ArrayList<SportsCatergory>()
    private lateinit var playingSportsBinding: FragmentPlayingSportsBinding
    private var selectedPlayingSports = ArrayList<SportsCatergory>()

    override fun sportsCategoryListener(sportsCategory: ArrayList<SportsCatergory>) {
        selectedPlayingSports = sportsCategory
        val bundle = Bundle()
        bundle.putString("name",arguments?.getString("name").toString())
        bundle.putString("city",arguments?.getString("city").toString())
        bundle.putString("gender",arguments?.getString("gender").toString())
        bundle.putString("phone",arguments?.getString("phone").toString())
        bundle.putString("country",arguments?.getString("country").toString())
        bundle.putParcelableArrayList("playingSportsList",selectedPlayingSports)
        findNavController().navigate(R.id.action_playingSports_to_intrestedSports,bundle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        playingSportsBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_playing_sports,container,false)
        playingSportsBinding.previousButton.setOnClickListener {
            findNavController().navigate(R.id.action_playingSports_to_basicInfo)
        }
        inflateSports()
        return playingSportsBinding.root
    }

    private fun inflateSports() {
        playingSportsBinding.recyclerViewPlayingSports.layoutManager = GridLayoutManager(context,3)
        firebaseDatabase.getReference("categories").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                categoryList.clear()
               p0.children.forEach{
                   categoryList.add(it.getValue(SportsCatergory::class.java)!!)
               }
                playingSportsBinding.recyclerViewPlayingSports.adapter = SelectSportsCategoriesAdapter(this@PlayingSports,
                    null,null,categoryList,playingSportsBinding.nextButton,null)
            }

        })
    }


}
