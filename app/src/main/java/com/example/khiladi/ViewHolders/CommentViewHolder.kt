package layout

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.Chat
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.R
import com.example.khiladi.fragments.CommentModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.comment_single_layout.view.*


class CommentViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.comment_single_layout,parent,false)) {

    private var imageView: ImageView? = null
    private var username: TextView? = null
    private var comment: TextView? = null

    init {

        imageView = itemView.findViewById(R.id.profile_imageView)
        username = itemView.findViewById(R.id.username)
        comment = itemView.findViewById(R.id.desc)


    }

    fun bind(commentModel: CommentModel) {
        comment?.text = commentModel.comment
        FirebaseDatabase.getInstance().getReference("khiladi/${commentModel.userId}")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    val khiladi = p0.getValue(Khiladi::class.java)
                    Picasso.get().load(khiladi?.profilepic).into(imageView)
                    username?.text = khiladi?.name
                }
            })
    }

}