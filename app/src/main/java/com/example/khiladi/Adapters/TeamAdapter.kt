package com.example.khiladi.Adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.fragments.CreateEvent
import com.example.khiladi.fragments.Teams
import com.example.khiladi.Models.Team
import layout.TeamViewHolder

class TeamAdapter(var team: ArrayList<Team>, var teams : Teams?, var createEvent: CreateEvent?): RecyclerView.Adapter<TeamViewHolder>(){
    private lateinit var teamListener : TeamListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        if (teams != null) teamListener = teams!!
        else teamListener = createEvent!!
        val inflater= LayoutInflater.from(parent.context)
        return TeamViewHolder(inflater,parent)
    }

    override fun getItemCount()= team.size


    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val team= team[position]
        holder.bind(team)
        holder.itemView.setOnClickListener {
            teamListener.callback(team)
        }
    }

    interface TeamListener{
        fun callback(team: Team)
    }
}