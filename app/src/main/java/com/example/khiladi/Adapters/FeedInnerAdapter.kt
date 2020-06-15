
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.Chat
import com.example.khiladi.Models.Feed
import com.example.khiladi.Models.Post
import com.example.khiladi.fragments.Feeds
import com.google.firebase.auth.FirebaseAuth
import layout.ChatLeftItemViewHolder
import layout.ChatRightItemViewHolder
import layout.ImageFeedViewHolder
import layout.VideoFeedViewHolder

class FeedInnerAdapter(
    var post: ArrayList<Post>,var feeds: Feeds
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post= post[position]
        if(holder.itemViewType == IMAGE) {
           val postHolder =  holder as ImageFeedViewHolder
            postHolder.bind(post.url!!)
        }
        else{
            val postHolder =  holder as VideoFeedViewHolder
            postHolder.bind(post.url!!,feeds)
        }
    }

    private val IMAGE = 1
    private val VIDEO = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater= LayoutInflater.from(parent.context)
        when(viewType){
                IMAGE-> return ImageFeedViewHolder(inflater,parent)
                VIDEO-> return VideoFeedViewHolder(inflater,parent)
        }
        return null!!
    }

    override fun getItemCount()= post.size


    override fun getItemViewType(position: Int): Int {
        val post = post.get(position)
        return if(post.type == "image") {
            IMAGE
        } else VIDEO

    }

}