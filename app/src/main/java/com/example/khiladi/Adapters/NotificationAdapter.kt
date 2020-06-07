package com.example.khiladi.Adapters
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.Notification
import com.example.khiladi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import layout.NotificationViewHolder

class NotificationAdapter(var notification: ArrayList<Notification>,var context:com.example.khiladi.fragments.Notification): RecyclerView.Adapter<NotificationViewHolder>(){

    interface BackToNotification{
        fun moveToTeamProfile(notification: Notification)
        fun moveToPlayerProfile(notification: Notification)
        fun delete(notification: Notification)
    }
    private lateinit var listener : BackToNotification
    var currentUser = FirebaseAuth.getInstance().currentUser
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        listener = context
        return NotificationViewHolder(inflater,parent)
    }

    override fun getItemCount()= notification.size


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {

        val notification= notification[position]
        holder.bind(notification,context)
        var respond = false
        holder.accept?.setOnClickListener {
            if(notification.accept == false){
                    holder.accept?.background = context.resources.getDrawable(R.drawable.respond_unfocus)
                    holder.accept?.text = "Accepted"
                    respond = true
                    holder.delete?.visibility = View.GONE
                    val player = HashMap<String,String>()
                player[currentUser?.uid!!] = currentUser?.uid!!
                    val team = HashMap<String,String>()
                team[notification.teamId] = notification.teamId
                    FirebaseDatabase.getInstance().getReference("notification/${FirebaseAuth.getInstance().currentUser?.uid}/${notification.id}/accept").setValue(true)
                FirebaseDatabase.getInstance().getReference("notification/${FirebaseAuth.getInstance().currentUser?.uid}/${notification.id}/read").setValue(true)
                FirebaseDatabase.getInstance().getReference("notification/${FirebaseAuth.getInstance().currentUser?.uid}/${notification.id}").child("respond").setValue(true).addOnSuccessListener {
                       if(notification.type == "requestJoinTeam") {
                           FirebaseDatabase.getInstance()
                               .getReference("teams/cricket/${notification.teamId}/players")
                               .updateChildren(player as Map<String, Any>).addOnSuccessListener {
                               FirebaseDatabase.getInstance()
                                   .getReference("khiladi/${currentUser?.uid}/teams/${notification.category}")
                                   .updateChildren(team as Map<String, Any>)
                           }
                       }
                    else if(notification.type == "event response"){
                           addToFixture(notification)
                       }

                    

                        //Toast.makeText(context,"updated",Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {
                        //Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                    }
            }


        }

        holder.layout?.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("notification/${FirebaseAuth.getInstance().currentUser?.uid}/${notification.id}/read").setValue(true)
            holder.layout!!.setBackgroundColor(context.resources.getColor(android.R.color.white))
            if(notification.type == "fan") listener.moveToPlayerProfile(notification)
            if(notification.type == "requestJoinTeam") listener.moveToTeamProfile(notification)
            if(notification.type == "event response") listener.moveToTeamProfile(notification)
        }

        holder.delete?.setOnClickListener {
            listener.delete(notification)
        }
    }

    private fun addToFixture(notification: Notification?) {
        FirebaseDatabase.getInstance().getReference("events/${notification?.category}/${notification?.eventRef}/team2").setValue(notification?.teamId)
        FirebaseDatabase.getInstance().getReference("events/${notification?.category}/${notification?.eventRef}/fixture").setValue(true)
    }

}