
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.fragments.Places
import com.example.khiladi.Models.Ads2
import kotlinx.android.synthetic.main.ads_single_layout.view.*
import layout.AdsViewHolder
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.khiladi.Models.Chat
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.Models.Team
import com.example.khiladi.R
import com.example.khiladi.TimeAgo
import com.example.khiladi.chat.LatestMessages
import com.example.khiladi.createEvent.NearbyPlaces
import com.example.khiladi.fragments.MyAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class LatestMessageAdapter(
    var latestMessages: com.example.khiladi.fragments.Chat,
    var chat: ArrayList<Chat>): RecyclerView.Adapter<LatestMessageViewHolder>(){

    private lateinit var messageCallback: MessageCallback
    interface MessageCallback{
        fun listener(chat: Chat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestMessageViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        messageCallback = latestMessages

        return LatestMessageViewHolder(inflater,parent)
    }

    override fun getItemCount()= chat.size


    override fun onBindViewHolder(holder: LatestMessageViewHolder, position: Int) {
        val chat = chat[position]
        holder.bind(chat)
        holder.itemView.setOnClickListener {
            messageCallback.listener(chat)
        }
    }


}

class LatestMessageViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.chat_single_layout,parent,false)) {

    private var imageView: ImageView? = null
    private var message: TextView? = null
    private var name :TextView? = null
    private var time : TextView? = null

    init {

        imageView = itemView.findViewById(R.id.imageView_chat)
        message = itemView.findViewById(R.id.latest_message_chat)
        name = itemView.findViewById(R.id.name_chat)
        time = itemView.findViewById(R.id.time_chat)


    }

    fun bind(chat: Chat) {

        if(chat.group == true){
            FirebaseDatabase.getInstance().getReference("teams/${chat.category}/${chat.id}").addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val team = p0.getValue(Team::class.java)
                    message?.text = chat.msg
                    time?.text = TimeAgo.getTimeAgo(chat.timeStamp!!.toLong())
                    name?.text = team?.name
                    Picasso.get().load(team?.profile).fit().into(imageView)
                }

            })

        }else {
            FirebaseDatabase.getInstance().getReference("khiladi/${chat.id}")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val khiladi = p0.getValue(Khiladi::class.java)
                        message?.text = chat.msg
                        time?.text = TimeAgo.getTimeAgo(chat.timeStamp!!.toLong())
                        name?.text = khiladi?.name
                        Picasso.get().load(khiladi?.profilepic).fit().into(imageView)
                    }
                })
        }
    }

}