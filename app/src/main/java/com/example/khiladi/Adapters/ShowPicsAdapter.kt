
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.R
import com.example.khiladi.Resgiter.IntrestedSports
import com.example.khiladi.Resgiter.PlayingSports
import com.example.khiladi.createPlaceAd.CustomGallery
import kotlinx.android.synthetic.main.fragment_playing_sports.*
import kotlinx.android.synthetic.main.select_sports_category_single_layout.view.*
import layout.SelectSportsCategoryViewHolder
import layout.ShowPicsViewHolder


class ShowPicsAdapter(var list : ArrayList<Uri>, var context : CustomGallery): RecyclerView.Adapter<ShowPicsViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowPicsViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        return ShowPicsViewHolder(inflater,parent)
    }

    override fun getItemCount()= list.size


    override fun onBindViewHolder(holder: ShowPicsViewHolder, position: Int) {
        val uri= list[position]
        holder.bind(uri)
    }

}

