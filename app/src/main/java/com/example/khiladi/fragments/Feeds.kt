package com.example.khiladi.fragments


import FeedAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.khiladi.Models.Feed
import com.example.khiladi.databinding.FragmentFeedsBinding
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.khiladi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class Feeds : Fragment(),FeedAdapter.CommentListener {

    lateinit var feedList :ArrayList<Feed>
    lateinit var binding: FragmentFeedsBinding
    var KEY =  "recycler_state"
    var CHECK = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, com.example.khiladi.R.layout.fragment_feeds,container,false)

        feedList = ArrayList()

        binding.profileImageView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("khiladiId",FirebaseAuth.getInstance().currentUser?.uid)
            findNavController().navigate(R.id.action_feed_to_khiladiProfile,bundle)
        }
        binding.desc.setOnClickListener {
            findNavController().navigate(R.id.action_feed_to_createPost)
        }
        Picasso.get().load(FirebaseAuth.getInstance().currentUser?.photoUrl).into(binding.profileImageView)



       FirebaseDatabase.getInstance().getReference("feeds").addValueEventListener(object : ValueEventListener{
           override fun onCancelled(p0: DatabaseError) {

           }


           override fun onDataChange(p0: DataSnapshot){
               feedList.clear()
               p0.children.forEach {
                   it.children.forEach {
                       val feed =  it.getValue(Feed::class.java)!!
                       feedList.add(feed)
                       val feedAdapter = FeedAdapter(feedList,this@Feeds)
                       binding.recyclerViewFeed.adapter = feedAdapter
                   }
               }


           }


          /* override fun onDataChange(p0: DataSnapshot) {
               feedList.clear()
               p0.children.forEach {
                   val feed =  it.getValue(Feed::class.java)
                   feedList.add(feed!!)
                   binding.recyclerViewFeed.adapter = FeedAdapter(feedList,this@Feeds)
               }
           }*/
       })
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY,binding.recyclerViewFeed.layoutManager!!.onSaveInstanceState())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState!=null){
            val state:Parcelable = savedInstanceState.getParcelable(KEY)!!
            binding.recyclerViewFeed.layoutManager!!.onRestoreInstanceState(state)
        }
    }

    override fun callback(feed: Feed) {
        val bundle = Bundle()
        bundle.putParcelable("feed",feed)
        findNavController().navigate(R.id.action_feed_to_commentFragment,bundle)
    }


}
