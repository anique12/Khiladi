package layout

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.custom.sliderimage.logic.SliderImage
import com.example.khiladi.Models.*
import com.example.khiladi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class AdsViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.ads_single_layout,parent,false)) {

    private var imageSlider: SliderImage? = null
    private var map: ImageView? = null
    private var profileImageView : ImageView? = null
    private var profileName : TextView? = null
    private var currentUserProfile : ImageView? = null
    private var currentUser : String? = null
    private var firebaseDatabase : FirebaseDatabase? = null
    private var khiladi : Khiladi? = null
    private var title : TextView? = null
    private var price : TextView? = null
    private var imageList : ArrayList<String>? = null
    private var locality : TextView? = null
    private var priceType : TextView? = null
    private var sportsCatergory : ImageView? = null
    private lateinit var select : Button

    init {

        imageSlider = itemView.findViewById(R.id.imageslider_ads)
        map = itemView.findViewById(R.id.map)
       // profileImageView = itemView.findViewById(R.id.profile_imageView_ads)
        title = itemView.findViewById(R.id.title)
        currentUserProfile = itemView.findViewById(R.id.currenUserProfileImage)
        currentUser =FirebaseAuth.getInstance().currentUser?.uid
        locality = itemView.findViewById(R.id.locality)
        sportsCatergory = itemView.findViewById(R.id.sportsCategory)
        select = itemView.findViewById(R.id.select)
      //  price = itemView.findViewById(R.id.pricePlaces)
       // priceType = itemView.findViewById(R.id.priceType)
        firebaseDatabase = FirebaseDatabase.getInstance()
        imageList = ArrayList()
        khiladi = Khiladi()

    }

    fun bind(ads: Ads2) {

       // price?.text = ads.price

        ads.images?.values?.forEach{
            imageList?.add(it)
            Log.d("imageListViewHolder",imageList.toString())
        }
        imageSlider?.setItems(imageList as List<String>)
        Picasso.get().load(ads.mapSnapshot).into(map)
        locality?.text = ads.locality
        title?.text = ads.title
        price?.text = ads.price + " PKR"
       // getCategory(ads.sportsId!!)
       // priceType?.text = ads.priceType

    }

    private fun getCategory(id:String) {
       firebaseDatabase?.getReference("categories/$id")?.addValueEventListener(object : ValueEventListener{
           override fun onCancelled(p0: DatabaseError) {

           }

           override fun onDataChange(p0: DataSnapshot) {
               val catergory = p0.getValue(SportsCatergory::class.java)
               Picasso.get().load(catergory?.photo).fit().into(sportsCatergory)
           }

       })
    }

}