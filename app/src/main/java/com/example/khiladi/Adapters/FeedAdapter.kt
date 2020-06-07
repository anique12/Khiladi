
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.Feed
import com.example.khiladi.Models.Notification
import layout.FeedViewHolder
import layout.NotificationViewHolder

class FeedAdapter(var feed: ArrayList<Feed>): RecyclerView.Adapter<FeedViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {

        val inflater= LayoutInflater.from(parent.context)
        return FeedViewHolder(inflater,parent)
    }

    override fun getItemCount()= feed.size


    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        var feed= feed[position]
        holder.bind(feed)
    }

}