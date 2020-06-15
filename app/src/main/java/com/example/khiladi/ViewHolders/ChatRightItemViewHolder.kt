package layout

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.Chat
import com.example.khiladi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso


class ChatRightItemViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.chat_to_row,parent,false)) {

    private var imageView: ImageView? = null
    private var message:TextView? = null

    init {

        imageView = itemView.findViewById(R.id.circle_image_view_chat_to_row)
        message = itemView.findViewById(R.id.messageChatToRow)

    }

    fun bind(chat: Chat,khiladiPic:String) {

        Picasso.get().load(FirebaseAuth.getInstance().currentUser?.photoUrl).fit().into(imageView)
        message?.text = chat.msg
    }

}