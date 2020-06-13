package layout

import FeedInnerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.Feed
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.R
import com.example.khiladi.TimeAgo
import com.example.khiladi.databinding.FragmentFeedsBinding
import com.example.khiladi.fragments.Feeds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class FeedViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.feed_single_layout,parent,false)) {

    private var recyclerView: RecyclerView? = null
    private var description: TextView? = null
    private var profileImageView : ImageView? = null
    private var profileName : TextView? = null
    private var like : ImageView? = null
    private var comment : ImageView? = null
    private var likes : TextView? = null
    private var likesList = ArrayList<String>()
    private var ts : TextView? = null
    private var categoryView : ImageView? = null



    init {

        recyclerView= itemView.findViewById(R.id.recyclerViewPost)
        description = itemView.findViewById(R.id.description_feed)
        profileImageView = itemView.findViewById(R.id.profile_imageView)
        profileName = itemView.findViewById(R.id.profile_name)
        like = itemView.findViewById(R.id.like)
        comment = itemView.findViewById(R.id.comment)
        likes = itemView.findViewById(R.id.likes)
        ts = itemView.findViewById(R.id.timeStamp)
        categoryView = itemView.findViewById(R.id.category)



    }

    fun bind(feed: Feed, feeds: Feeds,holder: FeedViewHolder) {

        if(feed.desc == ""){
            holder.description?.visibility = View.GONE
        }else description!!.text = feed.desc
        FirebaseAuth.getInstance().currentUser?.uid
        FirebaseDatabase.getInstance().getReference("khiladi/${feed.userId}").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                val khiladi = p0.getValue(Khiladi::class.java)
                Picasso.get().load(khiladi?.profilepic).fit().into(profileImageView)
                profileName!!.text = khiladi?.name
            }

        })
        getCategory(feed.catergory)
        getCommentNo(feed)
        ts!!.text = TimeAgo.getTimeAgo(feed.ts!!.toLong())
        getLikes(feed,holder)
        feed.postList?.let {
            recyclerView?.adapter = FeedInnerAdapter(it,feeds)
        }


    }

    private fun getCommentNo(feed: Feed) {
    }

    private fun getCategory(catergory: String?) {
        FirebaseDatabase.getInstance().getReference("categories/$catergory").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val category = p0.getValue(SportsCatergory::class.java)
                Picasso.get().load(category?.photo).into(categoryView)
            }
        })
    }

    private fun getLikes(feed: Feed,holder: FeedViewHolder) {
        FirebaseDatabase.getInstance().getReference("feeds/${feed.userId}/${feed.id}/likes").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
               p0.children.forEach {
                   likesList.add(it.value.toString())
               }
                if(likesList.isNotEmpty()) {
                    likes?.text = likesList.size.toString().plus(" likes")
                    holder.likes?.visibility = View.VISIBLE
                }
            }
        })

    }

}