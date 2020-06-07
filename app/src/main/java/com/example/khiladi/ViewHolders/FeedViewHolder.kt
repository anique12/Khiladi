package layout

import android.graphics.BitmapFactory
import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.Feed
import com.example.khiladi.Models.Notification
import com.example.khiladi.R
import com.squareup.picasso.Picasso
import java.net.URL


class FeedViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.image_layout_feeds,parent,false)) {

    private var imageView: ImageView? = null
    private var description: TextView? = null
    private var profileImageView : ImageView? = null
    private var profileName : TextView? = null
    private var currentUserProfile : ImageView? = null

    init {

        imageView = itemView.findViewById(R.id.imageView_feed)
        description = itemView.findViewById(R.id.description_feed)
        profileImageView = itemView.findViewById(R.id.profile_imageView)
        profileName = itemView.findViewById(R.id.profile_name)
        currentUserProfile = itemView.findViewById(R.id.currenUserProfileImage)

    }

    fun bind(feed: Feed) {

        description!!.text = feed.description
        Picasso.get().load(feed.imageView).fit().into(imageView)
        Picasso.get().load(feed.profileImage).fit().into(profileImageView)
        Picasso.get().load(feed.currentUserProfileImage).fit().into(currentUserProfile)
        profileName!!.text = feed.profileName

    }

}