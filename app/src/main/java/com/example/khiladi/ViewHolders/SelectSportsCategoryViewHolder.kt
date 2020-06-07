package layout

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.R
import com.squareup.picasso.Picasso


class SelectSportsCategoryViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.select_sports_category_single_layout,parent,false)) {

    private var imageView: ImageView? = null
    private var title:TextView? = null
    private var checkBox : CheckBox? = null


    init {
        imageView = itemView.findViewById(R.id.imageView)
        title = itemView.findViewById(R.id.textView2)
        checkBox = itemView.findViewById(R.id.checkBox)
    }

    fun bind(sportsCategory: SportsCatergory) {
        Picasso.get().load(sportsCategory.photo).fit().into(imageView)
        title?.text = sportsCategory.title
    }

}