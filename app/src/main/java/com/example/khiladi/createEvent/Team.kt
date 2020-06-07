package com.example.khiladi.createEvent


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Adapters.ChooseTeamAdapter
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.Models.Team

import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentTeamBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 */
class Team : Fragment(), ChooseTeamAdapter.TeamListener {

    override fun callback(team: Team) {
        val bundle = Bundle()
        bundle.putString("team",team.id)
        bundle.putString("sports",arguments?.getString("sports").toString())
        findNavController().navigate(R.id.action_team_to_nearbyPlaces,bundle)
    }

    private lateinit var teamBinding: FragmentTeamBinding
    var currentUser = FirebaseAuth.getInstance().currentUser
    var selectedCategory = String()
    var list = ArrayList<Team>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        teamBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_team, container, false)
        selectedCategory = arguments?.getString("sports").toString()
        inflateTeams()
        return teamBinding.root
    }

    private fun inflateTeams() {
        list.clear()
        FirebaseDatabase.getInstance().getReference("khiladi/${currentUser?.uid}/teams/${selectedCategory}/").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                    val id = it.value.toString()
                    FirebaseDatabase.getInstance().getReference("teams/${selectedCategory}/$id").addValueEventListener(object :
                        ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            val team = p0.getValue(Team::class.java)
                            if(team?.captainId == currentUser?.uid){
                                list.add(team!!)
                            }
                            teamBinding.recyclerViewTeam.adapter = ChooseTeamAdapter(list,null,null,this@Team)
                        }

                        override fun onCancelled(p0: DatabaseError) {
                        }

                    })
                }
            }

        })

    }


}
