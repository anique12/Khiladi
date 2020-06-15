package com.example.khiladi.fragments


import SportsSingleCategoriesAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.khiladi.Models.SportsCatergory

import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentBottomSheetDialogChooseCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 */
class BottomSheetDialogChooseCategory(createPost: CreatePost) : BottomSheetDialogFragment(),SportsSingleCategoriesAdapter.SportsSingleCategoryListener {

    override fun sportsCategoryListener(sportsCategory: ArrayList<SportsCatergory>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun selectedCategory(selectedSportsCategory: SportsCatergory) {
        selectedCategory = selectedSportsCategory
        this.dismiss()
        sportsCall.callBack(selectedCategory)
    }

    interface SportsCall{
        fun callBack(selectedCategory: SportsCatergory)
    }


    private var sportsCall = createPost
    private lateinit var bottomSheetBinding : FragmentBottomSheetDialogChooseCategoryBinding
    private var categoriesList = ArrayList<SportsCatergory>()
    private var selectedCategory = SportsCatergory()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bottomSheetBinding =  DataBindingUtil.inflate(inflater,
            R.layout.fragment_bottom_sheet_dialog_choose_category,
            container,
            false
        )

        FirebaseDatabase.getInstance().getReference("categories").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                    val categories = it.getValue(SportsCatergory::class.java)
                    categoriesList.add(categories!!)
                }
                bottomSheetBinding.recyclerView.adapter = SportsSingleCategoriesAdapter(context!!,categoriesList,null,
                    null,null,null,this@BottomSheetDialogChooseCategory)
            }
        })
        val manager = StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        bottomSheetBinding.recyclerView.layoutManager = manager

        return bottomSheetBinding.root
    }


}
