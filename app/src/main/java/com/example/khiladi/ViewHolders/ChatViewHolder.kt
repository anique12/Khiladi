package layout

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.Chat
import com.example.khiladi.R
import com.squareup.picasso.Picasso


class ChatViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.chat_single_layout,parent,false)) {

    private var imageView: ImageView? = null
    private var name:TextView? = null
    private var time: TextView? = null
    private var jobType: TextView? = null
    private var latestMessage:TextView? = null

    init {

        imageView = itemView.findViewById(R.id.imageView_chat)
        time = itemView.findViewById(R.id.time_chat)
        name = itemView.findViewById(R.id.name_chat)
        latestMessage = itemView.findViewById(R.id.latest_message_chat)

    }

    fun bind(chat: Chat) {

        name?.text = chat.name
        time?.text = chat.time
        Picasso.get().load(chat.url).fit().into(imageView)
        latestMessage?.text = chat.latestMessage
    }

}