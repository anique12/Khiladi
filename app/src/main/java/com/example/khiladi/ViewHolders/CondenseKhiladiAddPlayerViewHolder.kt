package layout

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.Chat
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.Models.Team
import com.example.khiladi.R
import com.squareup.picasso.Picasso


class CondenseKhiladiAddPlayerViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.condense_profile_add_player_single_layout,parent,false)) {

    private var imageView: ImageView? = null
    private var name:TextView? = null
    private var fans: TextView? = null
    var request : TextView? = null

    init {

        imageView = itemView.findViewById(R.id.imageViewCondenseProfileAddPlayer)
        name = itemView.findViewById(R.id.nameCondenseProfileAddPlayer)
        fans = itemView.findViewById(R.id.fansCondenseProfileAddPlayer)
        request = itemView.findViewById(R.id.requestCondenseProfileAddPlayer)

    }

    fun bind(khiladi: Khiladi) {

        name?.text = khiladi.name
        if (khiladi.profilepic != "") Picasso.get().load(khiladi.profilepic).fit().into(imageView)
        Log.d("profilepic",khiladi.profilepic)
        if (khiladi.fans == null)  fans?.text = "0 fans"
        else fans?.text = khiladi.fans!!.count().toString()+" fans"
    }

}