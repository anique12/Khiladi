
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.DialogChooseSingleSportsCategory
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.fragments.*
import layout.SportsSingleCategoryViewHolder


class SportsSingleCategoriesAdapter(val context:Context, var sportsCategory: List<SportsCatergory>,
                                    var discoverKhiladi: DiscoverKhiladi? = null,var teams : Teams? = null,
                                    var dialog : DialogChooseSingleSportsCategory? = null,var doneButton: Button? = null,var bottomSheetDialogChooseCategory: BottomSheetDialogChooseCategory? =null): RecyclerView.Adapter<SportsSingleCategoryViewHolder>(){

    private lateinit var listner : SportsSingleCategoryListener
    var list = ArrayList<SportsCatergory>()
    var selectedSportsCategory = SportsCatergory()

    interface SportsSingleCategoryListener{
        fun sportsCategoryListener(sportsCategory:ArrayList<SportsCatergory>)
        fun selectedCategory(selectedSportsCategory : SportsCatergory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportsSingleCategoryViewHolder {
        if(discoverKhiladi != null) listner = discoverKhiladi!!
        else if(teams != null) listner = teams!!
        else if(dialog != null) listner = dialog?.targetFragment as SportsSingleCategoryListener
        else if(bottomSheetDialogChooseCategory!=null) listner = bottomSheetDialogChooseCategory!!
        val inflater= LayoutInflater.from(parent.context)
        return SportsSingleCategoryViewHolder(inflater,parent)
    }

    override fun getItemCount()= sportsCategory.size


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: SportsSingleCategoryViewHolder, position: Int) {

        var sportsCategory= sportsCategory[position]
        holder.bind(sportsCategory)

        holder.itemView.setOnClickListener {

            if(dialog?.targetFragment == null){
                listner.selectedCategory(sportsCategory)

            }
            else{
                if (holder.itemView.elevation == 0f && list.size==1){

            }
            else{
                if (holder.itemView.elevation == 2f){
                    holder.itemView.setBackgroundResource(android.R.color.transparent)
                    holder.itemView.elevation = 0f
                    list.remove(sportsCategory)
                }
                else{
                    holder.itemView.setBackgroundResource(android.R.color.black)
                    holder.itemView.elevation = 2f
                    list.add(sportsCategory)
                }
            }


                doneButton?.setOnClickListener {
                    if (list != null){
                        listner.sportsCategoryListener(list)
                        dialog?.dismiss()
                    }
                }
            }

        }

    }

}

