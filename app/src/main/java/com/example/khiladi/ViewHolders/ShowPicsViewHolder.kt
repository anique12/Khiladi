package layout

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.R
import com.squareup.picasso.Picasso


class ShowPicsViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.show_pics_single_layout,parent,false)) {

    private var imageView: ImageView? = null



    init {
        imageView = itemView.findViewById(R.id.imageView)
    }

    fun bind(uri: Uri) {
        imageView?.setImageURI(uri)
    }

}