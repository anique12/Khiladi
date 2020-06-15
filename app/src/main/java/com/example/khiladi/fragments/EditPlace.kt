package com.example.khiladi.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Models.Ads2
import com.example.khiladi.Models.Slot
import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentEditPlaceBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class EditPlace : Fragment(),EditFragment.Editor,EditTimings.Callback {

    private lateinit var editPlaceBinding: FragmentEditPlaceBinding
    private var selectedPlace = Ads2()
    private var newTimingsList = ArrayList<String>()
    private var oldTimingList = ArrayList<ArrayList<Slot>>()
    private var otherTiming = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editPlaceBinding =  DataBindingUtil.inflate(inflater,
            R.layout.fragment_edit_place, container, false)
        selectedPlace = arguments?.getParcelable("place")!!

        editPlaceBinding.editTitle.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("title",selectedPlace.title)
            val fragment = EditFragment()
            fragment.setTargetFragment(this,1)
            fragment.arguments = bundle
            fragment.show(fragmentManager!!, "")

        }

        editPlaceBinding.save.setOnClickListener {
            Toast.makeText(context,"Updating",Toast.LENGTH_SHORT).show()
            FirebaseDatabase.getInstance().getReference("${selectedPlace.ref}/otherTimeResponce").setValue(otherTiming)
                .addOnSuccessListener {
                    Toast.makeText(context,"Updated",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                }
        }
        editPlaceBinding.otherTimings.setOnCheckedChangeListener { _, p1 ->
            otherTiming = p1
        }

        editPlaceBinding.editTimings.setOnClickListener {
            moveToEditTimings()
        }

        editPlaceBinding.editDescription.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("description",selectedPlace.title)
            val fragment = EditFragment()
            fragment.setTargetFragment(this,1)
            fragment.arguments = bundle
            fragment.show(fragmentManager!!, "")

        }
        inflateUI()
        return editPlaceBinding.root
    }

    private fun getTimingListFromDb() {
        FirebaseDatabase.getInstance().getReference("${selectedPlace.ref}/timings").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    Log.d("oldValue",it.value.toString())
                    val slotList = ArrayList<Slot>()
                    it.children.forEach {
                        slotList.add(it.getValue(Slot::class.java)!!)
                    }
                    oldTimingList.add(slotList)
                }
                editPlaceBinding.mondayTiming.text = oldTimingList[0][0].dayTime
                editPlaceBinding.tuesdayTiming.text = oldTimingList[1][0].dayTime
                editPlaceBinding.wednesdayTiming.text = oldTimingList[2][0].dayTime
                editPlaceBinding.thursdayTiming.text = oldTimingList[3][0].dayTime
                editPlaceBinding.fridayTiming.text = oldTimingList[4][0].dayTime
                editPlaceBinding.saturdayTiming.text = oldTimingList[5][0].dayTime
                editPlaceBinding.sundayTiming.text = oldTimingList[6][0].dayTime
            }
        })
    }

    private fun moveToEditTimings(){
        val fragment = EditTimings()
        fragment.setTargetFragment(this,1)
        val bundle = Bundle()
        bundle.putSerializable("oldTimings",oldTimingList)
        fragment.arguments = bundle
        fragment.show(fragmentManager!!, "")
    }

    private fun inflateUI() {
        editPlaceBinding.titleName.text = selectedPlace.title
        editPlaceBinding.title.text = selectedPlace.title
        editPlaceBinding.description.text = selectedPlace.description
        getTimingListFromDb()
    }

    override fun title(title: String) {
        FirebaseDatabase.getInstance().getReference("${selectedPlace.ref}/title").setValue(title)
            .addOnSuccessListener {
                Toast.makeText(context,"Updated",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
            }
    }

    override fun description(description: String) {
        FirebaseDatabase.getInstance().getReference("${selectedPlace.ref}/description").setValue(description)
            .addOnSuccessListener {
                Toast.makeText(context,"Updated",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
            }
    }

    override fun callback(list: ArrayList<String>) {
        newTimingsList = list
        updateTimingsInDb()
    }


    private fun updateTimingsInDb() {
        FirebaseDatabase.getInstance().getReference("${selectedPlace.ref}/timings").setValue(newTimingsList)
            .addOnSuccessListener {
                getTimingListFromDb()
                Toast.makeText(context,"Updated",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
            }
    }


}
