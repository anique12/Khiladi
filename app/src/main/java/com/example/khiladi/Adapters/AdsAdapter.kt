
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.fragments.Places
import com.example.khiladi.Models.Ads2
import kotlinx.android.synthetic.main.ads_single_layout.view.*
import layout.AdsViewHolder
import android.view.animation.AnimationUtils
import com.example.khiladi.R
import com.example.khiladi.createEvent.NearbyPlaces
import com.example.khiladi.fragments.MyAds


class AdsAdapter(
    var context: Context,
    var places: Places? = null,
    var ads: ArrayList<Ads2>, var nearbyPlaces : NearbyPlaces? = null
    ,var myAds: MyAds? = null): RecyclerView.Adapter<AdsViewHolder>(){
    private lateinit var toLeft: AnimationDrawable
    private lateinit var toRight: AnimationDrawable

    private lateinit var placeListener: PlaceListener
    interface PlaceListener{
        fun listener(ads2: Ads2)
        fun eventSelected(ads2: Ads2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        if(places!= null)  placeListener = places!!
        else if(nearbyPlaces!=null) placeListener = nearbyPlaces!!
        else if(myAds != null) placeListener = myAds!!

        return AdsViewHolder(inflater,parent)
    }

    override fun getItemCount()= ads.size


    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        val ads= ads[position]
        holder.bind(ads)
        if(nearbyPlaces!= null){
            holder.itemView.select.visibility = View.VISIBLE
        }

        holder.itemView.select.setOnClickListener {
            placeListener.eventSelected(ads)
        }


        holder.itemView.title.setOnClickListener {
            if(holder.itemView.expandableView.visibility == View.VISIBLE ){
                holder.itemView.expandableView.visibility = View.GONE
                holder.itemView.navigation.apply {
                    setBackgroundResource(R.drawable.left_to_right_arrow_rotation)
                    toRight = background as AnimationDrawable
                }
                toRight.start()
                holder.itemView.expandableView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.exit_to_left))
            }
            else{
                holder.itemView.expandableView.visibility = View.VISIBLE
                holder.itemView.navigation.apply {
                    setBackgroundResource(R.drawable.right_to_left_arrow_rotation)
                    toLeft = background as AnimationDrawable
                }
                toLeft.start()
                holder.itemView.expandableView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.enter_from_left))
            }
        }
        holder.itemView.navigation.setOnClickListener {

            if(holder.itemView.expandableView.visibility == View.VISIBLE ){
                holder.itemView.expandableView.visibility = View.GONE
                holder.itemView.navigation.apply {
                    setBackgroundResource(R.drawable.left_to_right_arrow_rotation)
                    toRight = background as AnimationDrawable
                }
                toRight.start()
                holder.itemView.expandableView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.exit_to_left))
            }
            else{
                holder.itemView.navigation.apply {
                    setBackgroundResource(R.drawable.right_to_left_arrow_rotation)
                    toLeft = background as AnimationDrawable
                }
                toLeft.start()
                holder.itemView.expandableView.visibility = View.VISIBLE
                holder.itemView.expandableView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.enter_from_left))
            }
        }
        holder.itemView.more.setOnClickListener {
            placeListener.listener(ads)
        }

    }


}