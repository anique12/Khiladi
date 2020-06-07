package layout

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.Chat
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.Models.Team
import com.example.khiladi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso


class CondenseKhiladiViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.condense_profile_single_layout,parent,false)) {

     var imageView: ImageView? = null
     var name:TextView? = null
     var fans: TextView? = null
     var fanBtn : TextView? = null
    var currentUser : FirebaseUser? =null

    init {

        imageView = itemView.findViewById(R.id.imageView_condenseProfile)
        name = itemView.findViewById(R.id.name_condenseProfile)
        fans = itemView.findViewById(R.id.fansCondenseProfile)
        fanBtn = itemView.findViewById(R.id.fansBtn)
        currentUser = FirebaseAuth.getInstance().currentUser
    }

    fun bind(khiladi: Khiladi) {

        name?.text = khiladi.name
        if (khiladi.profilepic != "") Picasso.get().load(khiladi.profilepic).fit().into(imageView)
        Log.d("profilepic",khiladi.profilepic)
        if (khiladi.fans == null)  fans?.text = "0 fans"
        else {
            fans?.text = khiladi.fans!!.count().toString()+" fans"
            if(khiladi.fans?.containsValue(currentUser?.uid)!!) fanBtn?.visibility = View.GONE
        }
    }

}