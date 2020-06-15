package com.example.khiladi.fragments

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Adapters.CommentAdapter
import com.example.khiladi.Models.Feed
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentCommentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.comment_single_layout.view.*
import kotlinx.android.synthetic.main.feed_single_layout.*


class CommentFragment : Fragment() {

    private lateinit var commentFragmentBinding : FragmentCommentBinding
    private lateinit var feed : Feed
    private var currentUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private lateinit var groupAdapter: GroupAdapter<GroupieViewHolder>
    private var commentList = ArrayList<CommentModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        commentFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_comment, container, false)
        feed = arguments?.getParcelable<Feed>("feed")!!
        getComment()
        commentFragmentBinding.send.setOnClickListener{
            if(commentFragmentBinding.comment.text.isNotEmpty()){
                setComment(commentFragmentBinding.comment.text.toString())
            }
            else Toast.makeText(context,"Cannot comment Empty message",Toast.LENGTH_SHORT).show()
        }
        return commentFragmentBinding.root
    }

    private fun setComment(comment: String) {
        val ref  = FirebaseDatabase.getInstance().getReference("comment/${feed.id}")
        val key = ref.push().key.toString()
        val mComment = CommentModel(key,feed.userId,comment,feed.id)
        ref.child(key).setValue(mComment)
            .addOnSuccessListener {
                Toast.makeText(context,"Comment Added",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
            }

        commentFragmentBinding.comment.text.clear()
    }

    private fun getComment() {

        val ref = FirebaseDatabase.getInstance().getReference("comment/${feed.id}")
        ref.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val comment = p0.getValue(CommentModel::class.java)
                commentList.add(comment!!)
                val adapter =  CommentAdapter(commentList)
                commentFragmentBinding.recyclerView.adapter = adapter
                adapter.notifyItemInserted(adapter.itemCount)

            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }





}

@Parcelize
data class CommentModel(val id :String? = null,val userId : String? = null, val comment:String? = null,var postId:String?=null):Parcelable