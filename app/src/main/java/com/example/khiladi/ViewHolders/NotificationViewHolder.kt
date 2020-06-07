package layout

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.Notification
import com.example.khiladi.R
import com.squareup.picasso.Picasso
import android.view.View
import android.widget.LinearLayout
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.Models.Team
import com.example.khiladi.TimeAgo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class NotificationViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.notification_single_layout,parent,false)) {

    private var imageView: ImageView? = null
    private var time: TextView? = null
    private var description: TextView? = null
    var accept : TextView? = null
    var layout : LinearLayout? = null
    var delete : TextView? = null

    init {

        imageView = itemView.findViewById(R.id.imageViewNotification)
        time = itemView.findViewById(R.id.timeStampNotification)
        description = itemView.findViewById(R.id.descriptionNotification)
        accept = itemView.findViewById(R.id.acceptNotification)
        layout = itemView.findViewById(R.id.notification)
        delete = itemView.findViewById(R.id.deleteNotification)

    }

    fun bind(notification: Notification,context:com.example.khiladi.fragments.Notification) {

        if(notification.type == "fan"){
            accept?.visibility = View.GONE
            delete?.visibility = View.GONE
        }



        if(notification.accept == true){
            delete?.visibility = View.GONE
        }
        if(notification.read == true) layout?.setBackgroundColor(context.resources.getColor(android.R.color.white))
            if(notification.accept == true){
                accept?.text = context.getString(R.string.accepted)
                accept?.background = context.resources.getDrawable(R.drawable.respond_unfocus)
            }
            var team : Team? =  Team()
            val current = FirebaseAuth.getInstance().currentUser?.uid
            FirebaseDatabase.getInstance().getReference("teams/${notification.category}/${notification.teamId}").addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    team = p0.getValue(Team::class.java)
                }

            })

            FirebaseDatabase.getInstance().getReference("khiladi/${notification.fromId}").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    val khiladi = p0.getValue(Khiladi::class.java)
                    time!!.text = TimeAgo.getTimeAgo(notification.time!!)
                    if(notification.type == "fan"){
                        if (khiladi?.profilepic!="") Picasso.get().load(khiladi?.profilepic).fit().into(imageView)
                        description!!.text = khiladi?.name+notification.description
                    }else if(notification.type == "event response"){
                        FirebaseDatabase.getInstance().getReference("teams/${notification.category}/${notification.teamId}/name").addValueEventListener(object :ValueEventListener{
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                val name = p0.value.toString()
                                description!!.text = name+notification.description
                                if (team?.profile!="") Picasso.get().load(team?.profile).fit().into(imageView)
                            }

                        })
                    }else{
                        if (khiladi?.profilepic!="") Picasso.get().load(khiladi?.profilepic).fit().into(imageView)
                        description!!.text = khiladi?.name+notification.description+" "+ team?.name
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }

            })

    }

}