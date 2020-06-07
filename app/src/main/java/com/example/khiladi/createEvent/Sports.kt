package com.example.khiladi.createEvent


import SelectSportsCategoriesAdapter
import android.os.Bundle
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
import com.example.khiladi.databinding.FragmentSportsAndTeamBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 */
class Sports : Fragment(),SelectSportsCategoriesAdapter.SelectedSingleCategoryListener {

    override fun sportsCategoryListener(sportsCategory: SportsCatergory) {
        val bundle = Bundle()
        bundle.putString("sports",sportsCategory.title)
        findNavController().navigate(R.id.action_sportsAndTeam_to_team,bundle)
    }

    private var firebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var sportsAndTeamBinding: FragmentSportsAndTeamBinding
    private var categoryList = ArrayList<SportsCatergory>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sportsAndTeamBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_sports_and_team, container, false)
        inflateSports()
        return sportsAndTeamBinding.root
    }


    private fun inflateSports() {
        sportsAndTeamBinding.recyclerViewCategories.layoutManager = GridLayoutManager(context,3)
        firebaseDatabase.getReference("categories").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                categoryList.clear()
                p0.children.forEach{
                    categoryList.add(it.getValue(SportsCatergory::class.java)!!)
                }
                sportsAndTeamBinding.recyclerViewCategories.adapter = SelectSportsCategoriesAdapter(null,null,
                    null,categoryList,
                    null,null,
                    null,this@Sports,sportsAndTeamBinding.nextButton)
            }

        })
    }

}
