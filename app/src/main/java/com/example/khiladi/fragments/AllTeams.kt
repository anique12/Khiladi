package com.example.khiladi.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.khiladi.Models.Team

import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentAllTeamsBinding

/**
 * A simple [Fragment] subclass.
 */
class AllTeams : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAllTeamsBinding>(inflater,R.layout.fragment_all_teams,container,false)
        var team = ArrayList<Team>()
        team.add(Team("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","Lahore Lions","Cricket","Open"))
        team.add(Team("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","Lahore Lions","Cricket","Open"))
        team.add(Team("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","Lahore Lions","Cricket","Open"))
        team.add(Team("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","Lahore Lions","Cricket","Open"))
        team.add(Team("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","Lahore Lions","Cricket","Open"))
        //binding.recyclerViewAllteams.adapter = com.example.khiladi.Adapters.TeamAdapter(team,this)

        return binding.root

    }

}
