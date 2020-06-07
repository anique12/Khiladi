package com.example.khiladi


import SportsSingleCategoriesAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.khiladi.Models.SportsCatergory

import com.example.khiladi.databinding.FragmentDialogChooseSportsCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class DialogChooseSingleSportsCategory : DialogFragment() {

    lateinit var dialogChooseSportsCategorybinding : FragmentDialogChooseSportsCategoryBinding
    private var chooseSportsCategory = ArrayList<SportsCatergory>()


 override fun onStart() {
  super.onStart()
  dialog!!.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
  activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

 }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       dialogChooseSportsCategorybinding = DataBindingUtil.inflate(inflater,R.layout.fragment_dialog_choose_sports_category,container,false)
        var list = ArrayList<SportsCatergory>()/*
        list.add(SportsCatergory("cricket","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcS3-KtExMO-J4Ug7vRkfAja4G95a5JI1_bHXzGsJTva0fSMd2ni","cricket"))
        list.add(SportsCatergory("football","https://www.telegraph.co.uk/content/dam/football/2019/08/15/TELEMMGLPICT000206110274_trans_NvBQzQNjv4BqrS8Z1b0ZQjNoViJZ3HnGQ4NS1YurETCFkeLSh1IwB7c.jpeg?imwidth=450","Football"))
        list.add(SportsCatergory("hockey","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSMSeF4bs0LdEYzbjOKr3CjqA0OYnzUxYKFS1hHaYlk_958L8HY","Hockey"))
        list.add(SportsCatergory("bowling","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTIEQ0w5Xw3M_QIr5bbj0XoTqt3QRlG_d_LmAC5LX26Z0Z6rrrX","Bowling"))*/

        FirebaseDatabase.getInstance().getReference("categories").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                    val sportsCatergory = it.getValue(SportsCatergory::class.java)
                    list.add(sportsCatergory!!)
                    dialogChooseSportsCategorybinding.chooseSportsRecyclerView.adapter = SportsSingleCategoriesAdapter(context!!,list,null,null,this@DialogChooseSingleSportsCategory,dialogChooseSportsCategorybinding.doneBtnChooseCategory)
                }
            }

        })
        val manager = StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

       dialogChooseSportsCategorybinding.chooseSportsRecyclerView.layoutManager = manager

        return dialogChooseSportsCategorybinding.root
    }

}
