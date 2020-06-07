
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.fragments.DialogChooseSportsCategory
import com.example.khiladi.Models.SportsCatergory
import layout.SportsCategoryViewHolder



class SportsCategoriesAdapter(val context:Context,var sportsCategory: List<SportsCatergory>, var dialog: DialogChooseSportsCategory, val doneButton:Button): RecyclerView.Adapter<SportsCategoryViewHolder>(){

    private lateinit var listner : SportsCategoryListener
    var list = ArrayList<SportsCatergory>()

    interface SportsCategoryListener{
        fun sportsCategoryListener(sportsCategory:ArrayList<SportsCatergory>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportsCategoryViewHolder {
        listner = dialog.targetFragment as SportsCategoryListener
        val inflater= LayoutInflater.from(parent.context)
        return SportsCategoryViewHolder(inflater,parent)
    }

    override fun getItemCount()= sportsCategory.size


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: SportsCategoryViewHolder, position: Int) {

        var sportsCategory= sportsCategory[position]
        holder.bind(sportsCategory)

        holder.itemView.setOnClickListener {

            if (holder.itemView.elevation == 0f && list.size==3){
                Toast.makeText(context,"You can select maximum three Sports",Toast.LENGTH_SHORT).show()
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

            doneButton.setOnClickListener {
                if (list != null){
                    listner.sportsCategoryListener(list)
                    dialog.dismiss()
                }
            }

        }

    }

}

