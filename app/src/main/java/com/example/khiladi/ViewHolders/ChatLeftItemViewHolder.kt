package layout

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.Chat
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class ChatLeftItemViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.chat_from_row,parent,false)) {

    private var imageView: ImageView? = null
    private var message:TextView? = null

    init {

        imageView = itemView.findViewById(R.id.circle_image_view_chat_from_row)
        message = itemView.findViewById(R.id.messageChatFromRow)

    }

    fun bind(chat: Chat,khiladiPic:String) {


        FirebaseDatabase.getInstance().getReference("khiladi/${chat.fromId}").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val khiladi = p0.getValue(Khiladi::class.java)
                Picasso.get().load(khiladi?.profilepic).fit().into(imageView)
                message?.text = chat.msg
            }
        })

    }

}