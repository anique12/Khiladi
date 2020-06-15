package com.example.khiladi.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentEditTimingsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_item_list_dialog_item.view.*
import kotlinx.android.synthetic.main.fragment_schedule.view.*
import java.util.*
import kotlin.collections.ArrayList

class EditTimings : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentEditTimingsBinding
    private var mondayStartTiming = String()
    private var mondayEndTiming = String()
    private var tuesdayStartTiming = String()
    private var tuesdayEndTiming = String()
    private var wednesdayStartTiming = String()
    private var wednesdayEndTiming = String()
    private var thursdayStartTiming = String()
    private var thursdayEndTiming = String()
    private var fridayStartTiming = String()
    private var fridayEndTiming = String()
    private var saturdayStartTiming = String()
    private var saturdayEndTiming = String()
    private var sundayStartTiming = String()
    private var sundayEndTiming = String()
    private lateinit var listener : Callback
    private var list : ArrayList<String>? = null
    private var oldTimingList = ArrayList<String>()

    interface Callback{
        fun callback(list : ArrayList<String>)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_edit_timings, container, false)
        oldTimingList = arguments?.getStringArrayList("oldTimings")!!
        updateUI()
        binding.monday.setOnClickListener {
            openClock("monday","start")
            openClock("monday","end")
        }
        binding.tuesday.setOnClickListener {
            openClock("tuesday","start")
            openClock("tuesday","end")
        }
        binding.wednesday.setOnClickListener {
            openClock("wednesday","start")
            openClock("wednesday","end")
        }
        binding.thursday.setOnClickListener {
            openClock("thursday","start")
            openClock("thursday","end")
        }
        binding.friday.setOnClickListener {
            openClock("friday","start")
            openClock("friday","end")
        }
        binding.saturday.setOnClickListener {
            openClock("saturday","start")
            openClock("saturday","end")
        }
        binding.sunday.setOnClickListener {
            openClock("sunday","start")
            openClock("sunday","end")
        }
        binding.save.setOnClickListener {
            list = ArrayList()
            Toast.makeText(context,"Updating",Toast.LENGTH_SHORT).show()
            list?.add(mondayStartTiming.plus(" - ").plus(mondayEndTiming))
            list?.add(tuesdayStartTiming.plus(" - ").plus(tuesdayEndTiming))
            list?.add(wednesdayStartTiming.plus(" - ").plus(wednesdayEndTiming))
            list?.add(thursdayStartTiming.plus(" - ").plus(thursdayEndTiming))
            list?.add(fridayStartTiming.plus(" - ").plus(fridayEndTiming))
            list?.add(saturdayStartTiming.plus(" - ").plus(saturdayEndTiming))
            list?.add(sundayStartTiming.plus(" - ").plus(sundayEndTiming))
            listener = targetFragment as EditPlace
            dismiss()
            listener.callback(list!!)
        }
        return binding.root
    }

    private fun updateUI() {
        binding.mondayText.text = oldTimingList[0]
        binding.tuesdayText.text = oldTimingList[1]
        binding.wednesdayText.text = oldTimingList[2]
        binding.thursdayText.text = oldTimingList[3]
        binding.fridayText.text = oldTimingList[4]
        binding.saturdayText.text = oldTimingList[5]
        binding.sundayText.text = oldTimingList[6]
        val l = oldTimingList[0].split("-")
        mondayStartTiming = l[0]
        mondayEndTiming = l[1]
        val m = oldTimingList[0].split("-")
        tuesdayStartTiming = m[0]
        tuesdayEndTiming = m[1]
        val n = oldTimingList[0].split("-")
        wednesdayStartTiming = n[0]
        wednesdayEndTiming = n[1]
        val o = oldTimingList[0].split("-")
        thursdayStartTiming = o[0]
        thursdayEndTiming = o[1]
        val p = oldTimingList[0].split("-")
        fridayStartTiming = p[0]
        fridayEndTiming = p[1]
        val q = oldTimingList[0].split("-")
        saturdayStartTiming = q[0]
        saturdayEndTiming = q[1]
        val r = oldTimingList[0].split("-")
        sundayStartTiming = r[0]
        sundayEndTiming = r[1]

    }


    private fun openClock(s: String,type:String) {
            val mcurrentTime = Calendar.getInstance()
            val hourOfDay = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener { timePicker, mselectedHour, selectedMinute ->
                    var selectedHour = mselectedHour
                    var AM_PM = " AM"
                    var mm_precede = ""
                    if (selectedHour >= 12) {
                        AM_PM = " PM"
                        if (selectedHour in 13..23) {
                            selectedHour -= 12
                        } else {
                            selectedHour = 12
                        }
                    } else if (selectedHour == 0) {
                        selectedHour = 12
                    }
                    if (minute < 10) {
                        mm_precede = "0"
                    }
                    if(type == "end"){
                        when(s){
                            "monday" -> {
                                val startTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                                mondayStartTiming = startTime.toString()
                            }
                            "tuesday" -> {
                                val startTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                                tuesdayStartTiming = startTime.toString()
                            }
                            "wednesday" -> {
                            val startTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                            wednesdayStartTiming = startTime.toString()
                        }
                            "thursday" ->{
                            val startTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                            thursdayStartTiming = startTime.toString()
                        }
                            "friday" ->{
                            val startTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                            fridayStartTiming = startTime.toString()
                        }
                            "saturday" -> {
                            val startTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                            saturdayStartTiming = startTime.toString()
                        }
                            "sunday" ->{
                           val startTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                            sundayStartTiming = startTime.toString()
                        }

                        }
                    }else if(type == "start"){
                        when(s){
                            "monday" ->{
                                val endTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                                mondayEndTiming = endTime.toString()
                                binding.mondayText.text = mondayStartTiming.plus(" - ").plus(mondayEndTiming)
                            }
                            "tuesday" -> {
                                val endTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                                tuesdayEndTiming = endTime.toString()
                                binding.tuesdayText.text = tuesdayStartTiming.plus(" - ").plus(tuesdayEndTiming)
                            }
                            "wednesday" ->{
                                val endTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                                wednesdayEndTiming = endTime.toString()
                                binding.wednesdayText.text = wednesdayStartTiming.plus(" - ").plus(wednesdayEndTiming)
                            }
                            "thursday" -> {
                                val endTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                                thursdayEndTiming = endTime.toString()
                                binding.thursdayText.text = thursdayStartTiming.plus(" - ").plus(thursdayEndTiming)
                            }
                            "friday" -> {
                                val endTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                                fridayEndTiming = endTime.toString()
                                binding.fridayText.text = fridayStartTiming.plus(" - ").plus(fridayEndTiming)
                            }
                            "saturday" ->{
                                val endTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                                saturdayEndTiming = endTime.toString()
                                binding.saturdayText.text = saturdayStartTiming.plus(" - ").plus(saturdayEndTiming)
                            }
                            "sunday" ->{
                                val endTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                                sundayEndTiming = endTime.toString()
                                binding.sundayText.text = sundayStartTiming.plus(" - ").plus(sundayEndTiming)
                            }
                        }
                    }

                }, hourOfDay, minute, false)

        if(type == "end") mTimePicker.setTitle("Select start Time")
        else if(type == "start")  mTimePicker.setTitle("Select end Time")
        mTimePicker.show()

    }

}