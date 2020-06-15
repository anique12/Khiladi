package com.example.khiladi.chat


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.khiladi.Models.Ads2
import com.example.khiladi.Models.Chat

import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentLatestMessagesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class LatestMessages : Fragment(),LatestMessageAdapter.MessageCallback {

    override fun listener(chat: Chat) {
        Toast.makeText(context,chat.msg,Toast.LENGTH_SHORT).show()
    }

    private lateinit var latestMessagesBinding: FragmentLatestMessagesBinding
    private  var latestMsgList = ArrayList<Chat>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        latestMessagesBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_latest_messages, container, false)
        getData()
        return latestMessagesBinding.root
    }

    private fun getData() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        FirebaseDatabase.getInstance().getReference("latestMessages/$currentUserId").addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                latestMsgList.clear()
                p0.children.forEach{
                    val chat = it.getValue(Chat::class.java)
                    latestMsgList.add(chat!!)
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                latestMsgList.clear()
                p0.children.forEach{
                    val chat = it.getValue(Chat::class.java)
                    latestMsgList.add(chat!!)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }


        })
    }


}
