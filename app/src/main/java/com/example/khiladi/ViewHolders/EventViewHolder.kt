package layout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.Ads2
import com.example.khiladi.Models.Event
import com.example.khiladi.Models.Team
import com.example.khiladi.R
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class EventViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.event_single_layout,parent,false)) {

    private var imageView1: ImageView? = null
    private var imageView2: ImageView? = null
    private var name1:TextView? = null
    private var name2:TextView? = null
    private var dateTime:TextView? = null
    private var venue:TextView? =null
    private var timeStamp:TextView? =null
    var respond : TextView? =null
    var time : TextView? =null
    var team1 : Team? = null
    var team2 : Team? = null
    var responceList : ArrayList<String>? = null
    var teamsList : ArrayList<Team>? = null
    private lateinit var statusEvent : TextView

    init {

        imageView1 = itemView.findViewById(R.id.imageViewTeam1)
        imageView2 = itemView.findViewById(R.id.imageViewTeam2)
        name1 = itemView.findViewById(R.id.teamName1)
        name2 = itemView.findViewById(R.id.teamName2)
        dateTime = itemView.findViewById(R.id.dateEvent)
        venue = itemView.findViewById(R.id.placeEvent)
        respond = itemView.findViewById(R.id.respondEvent)
        time = itemView.findViewById(R.id.timeEvent)
        statusEvent = itemView.findViewById(R.id.statusEvent)
        team1 = Team()
        team2 = Team()
        responceList = ArrayList()
        teamsList = ArrayList()


    }

    fun bind(event: Event) {

        if(event.fixture == true){
            statusEvent.text = "Fixture"
        }
        if (event.team2==null){
            FirebaseDatabase.getInstance().getReference("teams/${event.category}/${event.team1}").addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    team1 = p0.getValue(Team::class.java)
                    FirebaseDatabase.getInstance().getReference(event.placeRef!!).addValueEventListener(object :ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            val place = p0.getValue(Ads2::class.java)
                            name1?.text = team1?.name
                            name2?.text = "TDB"
                            timeStamp?.text = event.timeStamp
                            Picasso.get().load(team1?.profile).fit().into(imageView1)
                            dateTime?.text = event.date
                            venue?.text = place?.title
                            time?.text = event.time
                        }

                    })

                }

            })

        }

        else{
            FirebaseDatabase.getInstance().getReference("teams/${event.category}/${event.team2}").addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    team2 = p0.getValue(Team::class.java)
                    FirebaseDatabase.getInstance().getReference("teams/${event.category}/${event.team1}").addValueEventListener(object :ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                             team1 = p0.getValue(Team::class.java)
                            FirebaseDatabase.getInstance().getReference(event.placeRef!!).addValueEventListener(object : ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    val place = p0.getValue(Ads2::class.java)
                                    venue?.text = place?.title
                                    name1?.text = team1?.name
                                    name2?.text = team2?.name
                                    timeStamp?.text = event.timeStamp
                                    if (team1?.profile!="") Picasso.get().load(team1?.profile).fit().into(imageView1)
                                    if (team2?.profile!="") Picasso.get().load(team2?.profile).fit().into(imageView2)
                                    dateTime?.text = event.date
                                    time?.text = event.time
                                    respond?.visibility = View.INVISIBLE
                                }


                            })
                        }
                    })



                }

            })

        }

    }

}