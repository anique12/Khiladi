package com.example.khiladi.fragments


import ChatAdapter
import LatestMessageAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Models.Chat
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.Models.Team
import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Chat : Fragment(),LatestMessageAdapter.MessageCallback {

    var list = ArrayList<Chat>()
    private lateinit var chatBinding: FragmentChatBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        chatBinding = DataBindingUtil.inflate<FragmentChatBinding>(inflater,R.layout.fragment_chat,container,false)
        getData()


        return chatBinding.root
    }

    override fun listener(chat: Chat) {
        if(chat.group == true){
            FirebaseDatabase.getInstance().getReference("teams/${chat.category}/${chat.id}").addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val team = p0.getValue(Team::class.java)
                    val bundle = Bundle()
                    bundle.putParcelable("team",team)
                    findNavController().navigate(R.id.action_chat_to_chatLog,bundle)
                }
            })
        }else {
            FirebaseDatabase.getInstance().getReference("khiladi/${chat.toId}")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val khiladi = p0.getValue(Khiladi::class.java)
                        val bundle = Bundle()
                        bundle.putParcelable("khiladi", khiladi)
                        findNavController().navigate(R.id.action_chat_to_chatLog, bundle)
                    }
                })
        }
    }

    private fun getData() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        FirebaseDatabase.getInstance().getReference("latestMessages/$currentUserId").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                    val chat = it.getValue(Chat::class.java)
                    list.add(chat!!)
                }
                chatBinding.recyclerViewChat.adapter = LatestMessageAdapter(this@Chat,list)
            }


        })
    }

}
