package com.example.khiladi.fragments


import com.example.khiladi.Adapters.CondenseKhiladiAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.khiladi.DialogChooseSingleSportsCategory
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.NotificationHandling
import com.example.khiladi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import SportsSingleCategoriesAdapter
import android.util.DisplayMetrics
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.choose_category_bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_dialog_choose_sports_category.*
import kotlinx.android.synthetic.main.fragment_discover_khiladi.view.*





class DiscoverKhiladi : Fragment(), CondenseKhiladiAdapter.ChoosePlayersCallBack, CondenseKhiladiAdapter.FanListnerer,SportsSingleCategoriesAdapter.SportsSingleCategoryListener{

    override fun selectedCategory(selectedSportsCategory: SportsCatergory) {
        selectedCategory = selectedSportsCategory
        showUsers()
        discoverKhiadiView.firstView.text = selectedCategory.title
        Picasso.get().load(selectedCategory.photo).into(discoverKhiadiView.imageView)
        collapseBottomSheet()
    }

    private fun collapseBottomSheet() {
        val behavior = BottomSheetBehavior.from(discoverKhiadiView.chooseCategorySheet)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    lateinit var discoverKhiadiView: View
    lateinit var khiladilist: ArrayList<Khiladi>
    private var selectedKhiladi = Khiladi()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var currentUser = FirebaseAuth.getInstance().currentUser
    private var selectedCategory = SportsCatergory("-M28tl8D-yXJYjhiMHOX","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcS3-KtExMO-J4Ug7vRkfAja4G95a5JI1_bHXzGsJTva0fSMd2ni","cricket")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        discoverKhiadiView = inflater.inflate(R.layout.fragment_discover_khiladi,container,false)
       // discoverKhiladiBinding.sportsCategoryDiscoverPeople.setImageResource(R.drawable.cricket)

        inflateBottomSheet()
        khiladilist = ArrayList()
        showUsers()


       /* discoverKhiladiBinding.sportsCategoryDiscoverPeople.setOnClickListener {
            dialogFragment()
        }*/

        return discoverKhiadiView
    }

    override fun sportsCategoryListener(sportsCategory: ArrayList<SportsCatergory>) {
        selectedCategory = sportsCategory[0]
       // Picasso.get().load(selectedCategory.photo).into(discoverKhiladiBinding.sportsCategoryDiscoverPeople)
        showUsers()
    }

    override fun addFan(khiladi: Khiladi, textView: TextView) {
        val hashmap = HashMap<String,String>()
        hashmap[currentUser?.uid.toString()] = currentUser?.uid.toString()
        firebaseDatabase.getReference("khiladi/${khiladi.uid}/fans").updateChildren(hashmap as Map<String, Any>).addOnSuccessListener {
            textView.background = resources.getDrawable(R.drawable.respond_unfocus)
            Toast.makeText(context,"You are now fan of"+khiladi.name,Toast.LENGTH_SHORT).show()
            val notification = NotificationHandling(context!!)
            notification.sendFanNotification(khiladi.uid,currentUser?.uid!!)
        }
    }

    override fun callBack(khiladi: Khiladi) {
        selectedKhiladi = khiladi
        val bundle = Bundle()
        bundle.putParcelable("khiladi",selectedKhiladi)
        findNavController().navigate(R.id.action_discoverKhiladi_to_khiladiProfile,bundle)
    }


    private fun showUsers(){
        val firebaseDatabase = FirebaseDatabase.getInstance().getReference("users/${selectedCategory.title}")
        khiladilist.clear()
        firebaseDatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.hasChildren()){
                    p0.children.forEach{
                        val map = it.value!!.toString()
                        FirebaseDatabase.getInstance().getReference("khiladi/$map").addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if(p0.exists()){
                                    val khiladi = p0.getValue(Khiladi::class.java)
                                    khiladilist.add(khiladi!!)
                                    discoverKhiadiView.recyclerView_discoverKhiladi.adapter = CondenseKhiladiAdapter(khiladilist,this@DiscoverKhiladi,null)
                                }

                            }


                        })
                    }
                }
                else{
                    khiladilist.clear()
                    discoverKhiadiView.recyclerView_discoverKhiladi.adapter = CondenseKhiladiAdapter(khiladilist,this@DiscoverKhiladi,null)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, p0.message,Toast.LENGTH_SHORT).show()
            }

        })
    }

    private var list = ArrayList<SportsCatergory>()
    fun inflateBottomSheet(){
        discoverKhiadiView.firstView.text = selectedCategory.title
        Picasso.get().load(selectedCategory.photo).into(discoverKhiadiView.imageView)
        FirebaseDatabase.getInstance().getReference("categories").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                p0.children.forEach{
                    val sportsCatergory = it.getValue(SportsCatergory::class.java)
                    list.add(sportsCatergory!!)
                    discoverKhiadiView.recyclerViewCategoriesSports.adapter = SportsSingleCategoriesAdapter(context!!,list,this@DiscoverKhiladi,null)
                }
            }

        })
        val manager = StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        discoverKhiadiView.recyclerViewCategoriesSports.layoutManager = manager
    }
}
