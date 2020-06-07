package com.example.khiladi.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.fragments.*
import com.example.khiladi.Models.Team
import com.example.khiladi.R
import layout.ChooseTeamViewHolder

class ChooseTeamAdapter(var team: ArrayList<Team>, var teams : Teams?, var dialogChooseTeam: DialogChooseTeam? = null,
                        var teamEvent:com.example.khiladi.createEvent.Team? = null): RecyclerView.Adapter<ChooseTeamViewHolder>(){
    private lateinit var teamListener : TeamListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseTeamViewHolder {
        if(dialogChooseTeam != null)  teamListener = dialogChooseTeam?.targetFragment as Home
        else teamListener = teamEvent!!
        val inflater= LayoutInflater.from(parent.context)
        return ChooseTeamViewHolder(inflater,parent)
    }

    override fun getItemCount()= team.size


    override fun onBindViewHolder(holder: ChooseTeamViewHolder, position: Int) {
        val team = team[position]
        holder.bind(team)
        holder.itemView.setOnClickListener {
            dialogChooseTeam?.dismiss()
            teamListener.callback(team)
        }
    }

    interface TeamListener{
        fun callback(team: Team)
    }
}