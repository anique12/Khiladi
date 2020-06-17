package com.example.khiladi.fragments

import SlotsAdapter
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.khiladi.Models.Slot
import com.example.khiladi.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.fragment_book_place.view.*
import kotlinx.android.synthetic.main.slots_single_layout.*
import kotlinx.android.synthetic.main.slots_single_layout.view.*
import layout.SlotsViewHolder
import java.util.*
import kotlin.collections.ArrayList


class BookPlace : Fragment(),SlotsAdapter.SlotListener {

    private lateinit var bookPlaceView: View
    var datee : String? = null
    var time : String? = null
    private lateinit var horizontalCalendar : HorizontalCalendar
    private var dateNego = false
    private var timeNego = false
    private var placeNego = false
    private var urgent = false
    private var place = String()
    private var placeRef = String()
    private var selectedPlaceRef = "ads/cricket/pakistan/Karachi City/null/JiWmBOM5XRc0wkiLfXzXuDu5Iqz1/-M9ix4Cmuo0wQi3DoT2j"
    private var timingList = ArrayList<ArrayList<Slot>>()
    private var firebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bookPlaceView =  inflater.inflate(R.layout.fragment_book_place, container, false)
        val jsonList = arguments?.getString("timingList")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<ArrayList<Slot>>>(){}.type
        timingList = gson.fromJson(jsonList,type)
        Log.d("maslyHemasly",timingList.toString())
        Toast.makeText(context,timingList[0][0].dayTime.toString(),Toast.LENGTH_SHORT).show()
        val startDate = Calendar.getInstance()
        startDate.add(Calendar.DAY_OF_WEEK,0)

        /* end after 1 month from now */
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.DAY_OF_WEEK, 7)

        horizontalCalendar = HorizontalCalendar.Builder(bookPlaceView, R.id.calender)
            .range(startDate, endDate)
            .datesNumberOnScreen(5)
            .configure()
            .formatTopText("MMM")
            .formatMiddleText("dd")
            .formatBottomText("EEE")
            .textSize(14f, 24f, 14f)
            .showTopText(true)
            .showBottomText(true)
            .textColor(Color.LTGRAY, Color.WHITE)
            .end()
            .build()

        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar, position: Int) {
                datee = android.text.format.DateFormat.format("EEE, MMM d, yyyy", date).toString()
                //getDataFromDb()
                val c = Calendar.getInstance()
                c.time = Date(datee) // yourdate is an object of type Date
                val dayOfWeek = c[Calendar.DAY_OF_WEEK]
                when(dayOfWeek){
                    1 -> showTimings(timingList[0])
                    2 -> showTimings(timingList[1])
                    3 -> showTimings(timingList[2])
                    4 -> showTimings(timingList[3])
                    5 -> showTimings(timingList[4])
                    6 -> showTimings(timingList[5])
                    7 -> showTimings(timingList[6])

                }
            }

        }
        return bookPlaceView
    }

    private fun showTimings(arrayList: java.util.ArrayList<Slot>) {
        bookPlaceView.slotRecyclerView.adapter = SlotsAdapter(arrayList,this)
    }


    private fun getDataFromDb() {
        firebaseDatabase.getReference("${selectedPlaceRef}/timings").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                Toast.makeText(context,"getting data from db",Toast.LENGTH_SHORT).show()
                p0.children.forEach {
                    val string  = it.value.toString()
                    val timeList = string.split("-")
                    val startTime = getTimeIn24( timeList[0])
                    val endTime = getTimeIn24( timeList[1])
                    divideTimeInSlots(startTime,endTime)
                }
            }
        })
    }

    private fun divideTimeInSlots(startTime:Int,endTime:Int) {
        Toast.makeText(context,"dividing slots",Toast.LENGTH_SHORT).show()
        val list = ArrayList<String>()
        val slots = endTime - startTime
        Log.d("maslyYr",slots.toString())
        var time = startTime
        for(i in 1 until slots){
            time++
            val string : String = time.toString().plus(" - ").plus(time+1)
            list.add(string)
        }
        Log.d("maslyYr",list.toString())
    }

    private fun getTimeIn24(time : String): Int {

        return if(time.contains("PM")){
            val list = time.split(":")
            val newString = list[0].replace("\\s".toRegex(),"")
            val newint= newString.toInt()
            newint + 12
        } else {
            val list = time.split(":")
            val newString = list[0].replace("\\s".toRegex(),"")
            val newint= newString.toInt()
            newint
        }

    }

    override fun callback(slot: Slot,holder: SlotsViewHolder) {
        if(slot.book == true){
            bookPlaceView.book.isEnabled = false
            holder.itemView.setBackgroundColor(resources.getColor(R.color.com_facebook_button_background_color_disabled))
        }else{

        }
    }


}