package com.example.khiladi.chat


import ChatAdapter
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.khiladi.Models.Chat
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.Models.Team

import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentChatLogBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class ChatLog : Fragment() {

    private lateinit var chatBinding : FragmentChatLogBinding
    private var khiladi : Khiladi? = null
    private var team : Team? = null
    private var listMessages = ArrayList<Chat>()
    private var teamListMessages = ArrayList<Chat>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        chatBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_chat_log, container, false)

        if(arguments?.getParcelable<Khiladi>("khiladi")!=null){
            khiladi = arguments?.getParcelable<Khiladi>("khiladi")!!
            chatBinding.toolbar.title = khiladi?.name
            getKhiladiData()
        }
        if(arguments?.getParcelable<Team>("team")!=null){
            team = arguments?.getParcelable<Team>("team")!!
            chatBinding.toolbar.title = team?.name
            getTeamData()
        }

        chatBinding.send.setOnClickListener {
            if(!chatBinding.message.text.isEmpty()){
                if(khiladi!=null) sendKhiladiMessage()
             else if (team!=null) sendTeamMessage()
            }else
            Toast.makeText(context,"Cannot send empty message",Toast.LENGTH_SHORT).show()

        }
        return chatBinding.root
    }

    private fun sendTeamMessage() {
        val msg = chatBinding.message.text.toString()
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("teamChatMessages/${team?.id}")
        val key = ref.push().key
        val ts = System.currentTimeMillis()
        FirebaseDatabase.getInstance().getReference("categories/${team?.category}").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val category = p0.getValue(SportsCatergory::class.java)
                val chat = Chat(key,msg,ts.toString(),team?.id,currentUserId,true,category?.title)
                val chat1 = Chat(team?.id,msg,ts.toString(),team?.id,currentUserId,true,category?.title)
                FirebaseDatabase.getInstance().getReference("teamChatMessages/${team?.id}/$key").setValue(chat)
                FirebaseDatabase.getInstance().getReference("latestMessages/$currentUserId/${team?.id}").setValue(chat1)
            }
        })

        chatBinding.message.text.clear()

    }

    private fun getTeamData() {
        teamListMessages.clear()
        listMessages.clear()
        FirebaseDatabase.getInstance().getReference("teamChatMessages/${team?.id}").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                teamListMessages.clear()
                listMessages.clear()
                p0.children.forEach{
                    val chat = it.getValue(Chat::class.java)
                    teamListMessages.add(chat!!)

                }
                chatBinding.recyclerViewChatLog.adapter = ChatAdapter(teamListMessages,team?.profile!!)
                (chatBinding.recyclerViewChatLog.adapter as ChatAdapter).notifyDataSetChanged()
            }
        })
    }

    private fun getKhiladiData() {
        listMessages.clear()
        teamListMessages.clear()
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        FirebaseDatabase.getInstance().getReference("chatMessages/$currentUserId/${khiladi?.uid}").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                listMessages.clear()
                teamListMessages.clear()
                p0.children.forEach{
                    val chat = it.getValue(Chat::class.java)
                    listMessages.add(chat!!)
                }
                chatBinding.recyclerViewChatLog.adapter = ChatAdapter(listMessages,khiladi?.profilepic!!)
            }

        })
    }


    private fun sendKhiladiMessage() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val ref1  = FirebaseDatabase.getInstance().getReference("chatMessages/$currentUserId/${khiladi?.uid}")
        val key1 = ref1.push().key
        val ts = System.currentTimeMillis()
        val ref2 = FirebaseDatabase.getInstance().getReference("chatMessages/${khiladi?.uid}/${currentUserId}")
        val key2 = ref2.push().key
        val chat1 = Chat(key1,chatBinding.message.text.toString(),ts.toString(),khiladi?.uid,currentUserId)
        val chat2 = Chat(key2,chatBinding.message.text.toString(),ts.toString(),khiladi?.uid,currentUserId)
        val chat3 = Chat(khiladi?.uid,chatBinding.message.text.toString(),ts.toString(),khiladi?.uid,currentUserId)
        val chat4 = Chat(currentUserId,chatBinding.message.text.toString(),ts.toString(),khiladi?.uid,currentUserId)
        FirebaseDatabase.getInstance().getReference("chatMessages/$currentUserId/${khiladi?.uid}/$key1").setValue(chat1)
        FirebaseDatabase.getInstance().getReference("chatMessages/${khiladi?.uid}/${currentUserId}/$key2").setValue(chat2)
        FirebaseDatabase.getInstance().getReference("latestMessages/$currentUserId/${khiladi?.uid}").setValue(chat3)
        FirebaseDatabase.getInstance().getReference("latestMessages/${khiladi?.uid}/$currentUserId").setValue(chat4)
        chatBinding.message.text.clear()
    }


}
