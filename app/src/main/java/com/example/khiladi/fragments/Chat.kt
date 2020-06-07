package com.example.khiladi.fragments


import ChatAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentChatBinding

class Chat : Fragment() {

    lateinit var list:ArrayList<com.example.khiladi.Models.Chat>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentChatBinding>(inflater,R.layout.fragment_chat,container,false)
        //activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        list = ArrayList()
        list.add(com.example.khiladi.Models.Chat("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","Anique","12:31pm","Hey, i have gone through your profile and its amazing"))
        list.add(com.example.khiladi.Models.Chat("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","Anique","12:31pm","Hey, i have gone through your profile and its amazing"))
        list.add(com.example.khiladi.Models.Chat("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","Anique","12:31pm","Hey, i have gone through your profile and its amazing"))
        list.add(com.example.khiladi.Models.Chat("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","Anique","12:31pm","Hey, i have gone through your profile and its amazing"))

        binding.recyclerViewChat.adapter = ChatAdapter(list)


        return binding.root
    }


}
