package layout

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.Chat
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.Models.Team
import com.example.khiladi.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class TeamViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.team_single_layout,parent,false)) {

     var imageView: ImageView? = null
     var name:TextView? = null
     var staus: TextView? = null
     var category: TextView? = null

    init {

        imageView = itemView.findViewById(R.id.imageView_team)
        staus = itemView.findViewById(R.id.status_team)
        name = itemView.findViewById(R.id.name_team)
        category = itemView.findViewById(R.id.category_team)

    }

    fun bind(team: Team) {

        name?.text = team.name
        staus?.text = team.status
        Picasso.get().load(team.profile).fit().into(imageView)
        getTeam(team.category)
    }

    private fun getTeam(id : String) {
        FirebaseDatabase.getInstance().getReference("categories/$id").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val name = p0.getValue(SportsCatergory::class.java)?.title
                category?.text = name
            }

        })
    }

}