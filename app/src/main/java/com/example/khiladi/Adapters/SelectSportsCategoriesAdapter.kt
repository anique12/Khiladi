
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.R
import com.example.khiladi.Resgiter.IntrestedSports
import com.example.khiladi.Resgiter.PlayingSports
import com.example.khiladi.createEvent.Sports
import com.example.khiladi.createPlaceAd.SelectCategory
import kotlinx.android.synthetic.main.fragment_playing_sports.*
import kotlinx.android.synthetic.main.select_sports_category_single_layout.view.*
import layout.SelectSportsCategoryViewHolder


class SelectSportsCategoriesAdapter(var playingSports: PlayingSports? = null, var intrestedSports: IntrestedSports? = null
                                    , var category: SelectCategory? = null
                                    , var sportsList: ArrayList<SportsCatergory>
                                    , var nextButtonPlayingSports: Button? = null
                                    , var nextButtonIntrestedSports: Button? = null
                                    , var nextButtonCreateAd : Button? = null,
                                    var sportsAndTeam: Sports? = null,
                                    var nextBtnSportsAndTeam: Button? = null): RecyclerView.Adapter<SelectSportsCategoryViewHolder>(){

    private lateinit var listner : SelectedSportsCategoryListener
    private lateinit var listnererSingle : SelectedSingleCategoryListener
    var playingList = ArrayList<SportsCatergory>()
    var intrestedList = ArrayList<SportsCatergory>()
    var singleCategory : SportsCatergory? = null
    var lastItemView : View? = null

    interface SelectedSportsCategoryListener{
        fun sportsCategoryListener(sportsCategory:ArrayList<SportsCatergory>)
    }

    interface SelectedSingleCategoryListener{
        fun sportsCategoryListener(sportsCategory:SportsCatergory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectSportsCategoryViewHolder {
        if(intrestedSports!=null) listner = intrestedSports!!
        else if(playingSports!=null) listner = playingSports!!
        if(category!=null){
            listnererSingle = category!!
        }
        if(sportsAndTeam != null){
            listnererSingle = sportsAndTeam!!
        }
        val inflater= LayoutInflater.from(parent.context)
        return SelectSportsCategoryViewHolder(inflater,parent)
    }

    override fun getItemCount()= sportsList.size


    override fun onBindViewHolder(holder: SelectSportsCategoryViewHolder, position: Int) {

        val sportsCategory= sportsList[position]
        holder.bind(sportsCategory)
        if(intrestedSports!=null){
            holder.itemView.checkBox.isChecked = true
            intrestedList.add(sportsCategory)
        }

            holder.itemView.setOnClickListener {
                if(playingSports!=null){
                    if(holder.itemView.checkBox.isChecked){
                        playingList.remove(sportsCategory)
                        holder.itemView.checkBox.isChecked = false
                    }
                    else {
                        if(playingList.size == 3){
                            playingSports!!.description.setTextColor(playingSports!!.resources.getColor(android.R.color.holo_red_dark))
                            playingSports!!.description.startAnimation(AnimationUtils.loadAnimation(playingSports!!.context,R.anim.shake))
                        }
                        else{
                            holder.itemView.checkBox.isChecked = true
                            playingList.add(sportsCategory)
                        }
                    }
                }
                else if (intrestedSports!=null){
                    if(!holder.itemView.checkBox.isChecked){
                        holder.itemView.checkBox.isChecked = true
                        intrestedList.add(sportsCategory)
                    }
                    else{
                        holder.itemView.checkBox.isChecked = false
                        intrestedList.remove(sportsCategory)
                    }
                }
                else{
                    if(holder.itemView.checkBox.isChecked){
                        holder.itemView.checkBox.isChecked = false
                        singleCategory = null
                        lastItemView = null
                    }
                    else{
                        if(lastItemView != null){
                             lastItemView?.checkBox?.isChecked = false
                            holder.itemView.checkBox.isChecked = true
                             lastItemView = holder.itemView
                             singleCategory = sportsCategory

                        }else{
                            lastItemView = holder.itemView
                            singleCategory = sportsCategory
                            holder.itemView.checkBox.isChecked = true
                        }
                    }
                }
            }
            holder.itemView.checkBox.setOnClickListener {
                if(playingSports!=null){
                    if(holder.itemView.checkBox.isChecked){
                        playingList.add(sportsCategory)
                        if(playingList.size == 4) {
                            playingSports!!.description.setTextColor(playingSports!!.resources.getColor(android.R.color.holo_red_dark))
                            playingSports!!.description.startAnimation(AnimationUtils.loadAnimation(playingSports!!.context,R.anim.shake))
                            holder.itemView.checkBox.isChecked = false
                            playingList.remove(sportsCategory)
                        }
                    }else{
                        playingList.remove(sportsCategory)
                    }
                }
                else if(intrestedSports!=null){
                    if(!holder.itemView.checkBox.isChecked){
                        intrestedList.remove(sportsCategory)
                    }
                    else{
                        intrestedList.add(sportsCategory)
                    }
                }
                else {
                    if(!holder.itemView.checkBox.isChecked){
                        holder.itemView.checkBox.isChecked = false
                        singleCategory = null
                        lastItemView = null
                    }
                    else{
                        if(lastItemView != null){
                            lastItemView?.checkBox?.isChecked = false
                            lastItemView = holder.itemView
                            singleCategory = sportsCategory

                        }else{
                            lastItemView = holder.itemView
                            singleCategory = sportsCategory
                        }
                    }
                }
            }
            nextButtonPlayingSports?.setOnClickListener {
                if(playingList.isEmpty()){
                    Toast.makeText(playingSports!!.context,"Select at least one Sports",Toast.LENGTH_SHORT).show()
                }else{
                    listner.sportsCategoryListener(playingList)
                }
            }
            nextButtonIntrestedSports?.setOnClickListener {
                if(intrestedList.isEmpty()){
                    Toast.makeText(intrestedSports!!.context,"Select at least one Sports",Toast.LENGTH_SHORT).show()
                }else{
                    listner.sportsCategoryListener(intrestedList)
                }
            }
            nextButtonCreateAd?.setOnClickListener {
                if(singleCategory!=null){
                    listnererSingle.sportsCategoryListener(singleCategory!!)
                }
                else{
                    //category?. .startAnimation(AnimationUtils.loadAnimation(category?.context,R.anim.shake))
                }
            }
        nextBtnSportsAndTeam?.setOnClickListener {
            if(singleCategory!=null){
                listnererSingle.sportsCategoryListener(singleCategory!!)
            }
            else{
                //category?. .startAnimation(AnimationUtils.loadAnimation(category?.context,R.anim.shake))
            }
        }
    }

}

