
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.Feed
import com.example.khiladi.databinding.FragmentFeedsBinding
import com.example.khiladi.fragments.Feeds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.feed_single_layout.view.*
import layout.FeedViewHolder

class FeedAdapter(
    var feedList: ArrayList<Feed>,
    var feedFragment: Feeds
): RecyclerView.Adapter<FeedViewHolder>(){


    private var currentUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private lateinit var listener : CommentListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        listener = feedFragment
        return FeedViewHolder(inflater,parent)
    }

    override fun getItemCount()= feedList.size

        fun notifyItemInserted() {
            this.notifyItemInserted(itemCount)
        }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val feed= feedList[position]
        holder.bind(feed,feedFragment,holder)
        holder.itemView.like.setOnClickListener{
            addLike(feed,feedFragment,holder)
        }
        holder.itemView.comment.setOnClickListener {
            listener.callback(feed)
        }
    }

    private fun addLike(feed: Feed,feeds: Feeds,holder: FeedViewHolder) {
        val like = HashMap<String,String>()
        like[currentUser] = currentUser
        FirebaseDatabase.getInstance().getReference("feeds/${feed.userId}/${feed.id}/likes").updateChildren(like as Map<String, Any>)
            .addOnSuccessListener {
                Toast.makeText(feeds.context,"Liked",Toast.LENGTH_SHORT).show()
                holder.itemView.likes.visibility = View.VISIBLE
            }.addOnFailureListener{
                Toast.makeText(feeds.context,it.message,Toast.LENGTH_SHORT).show()
            }
    }

    interface CommentListener{
        fun callback(feed: Feed)
    }
}