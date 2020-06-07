package com.example.khiladi.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.fragments.DiscoverKhiladi
import com.example.khiladi.fragments.TeamProfile
import layout.CondenseKhiladiViewHolder

class CondenseKhiladiAdapter(var khiladi: ArrayList<Khiladi>, var discoverKhiladi: DiscoverKhiladi?, var teamProfile : TeamProfile?): RecyclerView.Adapter<CondenseKhiladiViewHolder>(){

    private lateinit var choosePlayersCallBack: ChoosePlayersCallBack
    private lateinit var fanListener: FanListnerer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CondenseKhiladiViewHolder {
        choosePlayersCallBack = if(discoverKhiladi!=null) discoverKhiladi!! else teamProfile!!
        fanListener = if(discoverKhiladi!=null) discoverKhiladi!! else teamProfile!!
        val inflater= LayoutInflater.from(parent.context)
        return CondenseKhiladiViewHolder(inflater,parent)
    }

    override fun getItemCount() = khiladi.size


    override fun onBindViewHolder(holder: CondenseKhiladiViewHolder, position: Int) {
        val selectedKhiladi= khiladi[position]
        holder.bind(selectedKhiladi)
        holder.itemView.setOnClickListener {
            choosePlayersCallBack.callBack(selectedKhiladi)
        }
        holder.fanBtn?.setOnClickListener {
            fanListener.addFan(selectedKhiladi,holder.fanBtn!!)
        }
    }


    interface ChoosePlayersCallBack{
        fun callBack(khiladi: Khiladi)
    }

    interface FanListnerer{
        fun addFan(khiladi: Khiladi,textView: TextView)
    }
}