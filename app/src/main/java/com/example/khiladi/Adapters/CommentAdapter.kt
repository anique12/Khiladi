package com.example.khiladi.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.fragments.Home
import com.example.khiladi.Models.Event
import com.example.khiladi.fragments.CommentModel
import layout.CommentViewHolder
import layout.EventViewHolder

class CommentAdapter(var comment: ArrayList<CommentModel>): RecyclerView.Adapter<CommentViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        return CommentViewHolder(inflater,parent)
    }

    override fun getItemCount()= comment.size


    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment= comment[position]
        holder.bind(comment)

    }


}