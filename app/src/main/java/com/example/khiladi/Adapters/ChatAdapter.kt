
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.Chat
import com.google.firebase.auth.FirebaseAuth
import layout.ChatLeftItemViewHolder
import layout.ChatRightItemViewHolder

class ChatAdapter(
    var chat: ArrayList<Chat>,
    var profilepic: String
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var chat= chat[position]
        if(holder.itemViewType == VIEW_TYPE_MESSAGE_SENT) {
           val chatHolder =  holder as ChatRightItemViewHolder
            chatHolder.bind(chat,profilepic)
        }
        else{
            val chatHolder =  holder as ChatLeftItemViewHolder
            chatHolder.bind(chat,profilepic)
        }
    }

    private val VIEW_TYPE_MESSAGE_SENT = 1
    private val VIEW_TYPE_MESSAGE_RECEIVED = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater= LayoutInflater.from(parent.context)
        when(viewType){
                VIEW_TYPE_MESSAGE_SENT-> return ChatRightItemViewHolder(inflater,parent)
                VIEW_TYPE_MESSAGE_RECEIVED-> return ChatLeftItemViewHolder(inflater,parent)
        }
        return null!!
    }

    override fun getItemCount()= chat.size


    override fun getItemViewType(position: Int): Int {
        val chat = chat.get(position)
        if(chat.fromId == FirebaseAuth.getInstance().currentUser?.uid) {
            return VIEW_TYPE_MESSAGE_SENT
        }
        else return VIEW_TYPE_MESSAGE_RECEIVED

    }

}