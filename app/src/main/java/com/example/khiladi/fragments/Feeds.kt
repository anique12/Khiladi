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
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class Feeds : Fragment() {

    lateinit var feedList :ArrayList<Feed>
    lateinit var binding: FragmentFeedsBinding
    var KEY =  "recycler_state"

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



        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
       FirebaseDatabase.getInstance().getReference("feeds/$currentUserId").addValueEventListener(object :ValueEventListener{
           override fun onCancelled(p0: DatabaseError) {

           }

           override fun onDataChange(p0: DataSnapshot) {
               p0.children.forEach {
                   val feed =  it.getValue(Feed::class.java)
                   feedList.add(feed!!)
                   binding.recyclerViewFeed.adapter = FeedAdapter(feedList)
               }
           }
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


}
