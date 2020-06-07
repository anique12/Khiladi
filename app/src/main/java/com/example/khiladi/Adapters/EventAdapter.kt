package com.example.khiladi.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.fragments.Home
import com.example.khiladi.Models.Event
import layout.EventViewHolder

class EventAdapter(var event: ArrayList<Event>,var context:Home): RecyclerView.Adapter<EventViewHolder>(){

    private lateinit var listener : ResponceListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        listener = context
        val inflater= LayoutInflater.from(parent.context)
        return EventViewHolder(inflater,parent)
    }

    override fun getItemCount()= event.size


    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event= event[position]
        holder.bind(event)
        holder.respond?.setOnClickListener {
            listener.sendResponce(event)
        }
    }

    interface ResponceListener{
        fun sendResponce(event: Event)
    }

}