package layout

import android.util.Log
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


class ImageFeedViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.image_layout_feeds,parent,false)) {

    private var imageView: ImageView? = null

    init {

        imageView = itemView.findViewById(R.id.imageView_feed)

    }

    fun bind(url : String) {
        Log.d("inFeedViewHolder2",url)
        Picasso.get().load(url).into(imageView)
    }

}