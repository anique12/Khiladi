
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.khiladi.fragments.CreateTeam
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.R
import layout.CondenseKhiladiAddPlayerViewHolder

@Suppress("DEPRECATION")
class CondenseKhiladiAddPlayerAdapter(var khiladi: ArrayList<Khiladi>, var context : CreateTeam, var doneButton : Button): RecyclerView.Adapter<CondenseKhiladiAddPlayerViewHolder>(){

    private lateinit var choosePlayersCallBack: ChoosePlayersCallBack
    var requestedTeam = ArrayList<Khiladi>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CondenseKhiladiAddPlayerViewHolder {
        choosePlayersCallBack = context
        val inflater= LayoutInflater.from(parent.context)
        return CondenseKhiladiAddPlayerViewHolder(inflater,parent)
    }

    override fun getItemCount()= khiladi.size


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: CondenseKhiladiAddPlayerViewHolder, position: Int) {
        var khiladi= khiladi[position]
        holder.bind(khiladi)
        holder.request?.setOnClickListener {
            Toast.makeText(context.activity,"click",Toast.LENGTH_LONG).show()
            if (holder.request?.elevation == 0f){
                requestedTeam.add(khiladi)
                Toast.makeText(context.activity,"add",Toast.LENGTH_LONG).show()
                holder.request?.background = context.resources.getDrawable(R.drawable.respond_unfocus)
                holder.request?.elevation = 2f
            }
            else if (holder.request?.elevation == 2f){
                    requestedTeam.remove(khiladi)
                Toast.makeText(context.activity,"remove",Toast.LENGTH_LONG).show()
                holder.request?.background = context.resources.getDrawable(R.drawable.respond)
                holder.request?.elevation = 0f

            }
        }

        doneButton.setOnClickListener {
            choosePlayersCallBack.callBack(requestedTeam)
        }
    }

    interface ChoosePlayersCallBack{
        fun callBack(khiladi: ArrayList<Khiladi>)
    }
}