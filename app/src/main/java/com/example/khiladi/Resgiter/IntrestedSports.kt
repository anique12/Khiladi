package com.example.khiladi.Resgiter


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
import com.example.khiladi.databinding.FragmentIntrestedSportsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_intrested_sports.view.*

/**
 * A simple [Fragment] subclass.
 */
class IntrestedSports : Fragment(),SelectSportsCategoriesAdapter.SelectedSportsCategoryListener {

    private lateinit var intrestedSportsBinding: FragmentIntrestedSportsBinding
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var categoryList = ArrayList<SportsCatergory>()
    private var selectedIntrestedSports = ArrayList<SportsCatergory>()

    override fun sportsCategoryListener(sportsCategory: ArrayList<SportsCatergory>) {
        selectedIntrestedSports = sportsCategory
        val bundle = Bundle()
        bundle.putString("name",arguments?.getString("name").toString())
        bundle.putString("city",arguments?.getString("city").toString())
        bundle.putString("gender",arguments?.getString("gender").toString())
        bundle.putString("phone",arguments?.getString("phone").toString())
        bundle.putString("country",arguments?.getString("country").toString())
        bundle.putParcelableArrayList("playingSportsList",arguments?.getParcelableArrayList("playingSportsList"))
        bundle.putParcelableArrayList("interestedSportsList",selectedIntrestedSports)
        findNavController().navigate(R.id.action_intrestedSports_to_profilePhoto,bundle)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        intrestedSportsBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_intrested_sports, container, false)
        inflateSports()
        intrestedSportsBinding.previousButton.setOnClickListener {
            findNavController().navigate(R.id.action_intrestedSports_to_playingSports)
        }
        return intrestedSportsBinding.root
    }

    private fun inflateSports() {
        intrestedSportsBinding.recyclerViewIntrestedSports.layoutManager = GridLayoutManager(context,3)
        firebaseDatabase.getReference("categories").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                categoryList.clear()
                p0.children.forEach{
                    categoryList.add(it.getValue(SportsCatergory::class.java)!!)
                }
                intrestedSportsBinding.recyclerViewIntrestedSports.adapter = SelectSportsCategoriesAdapter(null,
                    this@IntrestedSports,null,categoryList,null,intrestedSportsBinding.nextButton)
            }

        })
    }


}
