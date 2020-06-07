package layout

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.R
import com.squareup.picasso.Picasso


class SportsSingleCategoryViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.sports_category_single_layout,parent,false)) {

    private var imageView: ImageView? = null
    private var title:TextView? = null


    init {

        imageView = itemView.findViewById(R.id.imageView_sportsCategory)
        title = itemView.findViewById(R.id.title_sports_category)


    }

    fun bind(sportsCategory: SportsCatergory) {

        Picasso.get().load(sportsCategory.photo).fit().into(imageView)
        title?.text = sportsCategory.title
    }

}