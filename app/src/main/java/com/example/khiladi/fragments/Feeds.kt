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


class Feeds : Fragment() {

    lateinit var feed :ArrayList<Feed>
    lateinit var binding: FragmentFeedsBinding
    var KEY =  "recycler_state"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, com.example.khiladi.R.layout.fragment_feeds,container,false)

        feed = ArrayList()


        feed.add(Feed("https://picsum.photos/id/237/200/300","This is a dog","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","anique","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5"))
        feed.add(Feed("https://static01.nyt.com/newsgraphics/2016/12/22/trump-nuclear-tweet/a92addeafafef2a57f38430176608e414e351979/tweet.png","This is a dog","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","anique","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5"))
        feed.add(Feed("https://picsum.photos/id/237/200/300","This is a dog Combustor-The furnace consists of a 7.5 cm (2.95 in) diameter, 20.04 cm (79 in) long downward fired combustor. The furnace is made up of a steel frame containing a 13.5 cm (5.31 in) layer of insulation and refractory. Furnace is also consisting of 3 ports for air feeding or of flue gas sample collection are located at 60 cm [port 1), 103 cm [port 2) and 146 cm [port 3). The air is supplied with the help of air compressor into the burner. The air can be divided into three streams: primary air is introduced from top of the furnace with the fuel feed and the secondary 2, is introduced to the combustor via port 1 located at 60 cm from top, tertiary air can also be supplied from port 2 or from port 3. Both primary air and secondary air can be provided at different flow rate to assist fuel feeding to ensure the complete combustion. ","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","anique","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5"))
        feed.add(Feed("https://picsum.photos/id/237/200/300","This is a dog","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","anique","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5"))
        feed.add(Feed("https://picsum.photos/id/237/200/300","This is a dog","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","anique","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5"))
        feed.add(Feed("https://picsum.photos/id/237/200/300","This is a dog","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","anique","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5"))
        feed.add(Feed("https://picsum.photos/id/237/200/300","This is a dog","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","anique","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5"))
        feed.add(Feed("https://picsum.photos/id/237/200/300","This is a dog","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","anique","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5"))
        feed.add(Feed("https://picsum.photos/id/237/200/300","This is a dog","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","anique","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5"))
        feed.add(Feed("https://picsum.photos/id/237/200/300","This is a dog","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","anique","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5"))
        feed.add(Feed("https://picsum.photos/id/237/200/300","This is a dog","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5","anique","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRBpOFoYnHzWE0GFosYlYFF57SssQYGH2RiIUuBfaL43U0anJB5"))

        binding.recyclerViewFeed.adapter = FeedAdapter(feed)

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
