package com.example.khiladi.createPlaceAd


import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Models.Slot
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.R
import com.example.khiladi.Utils
import com.example.khiladi.databinding.FragmentDescriptionBinding
import com.example.khiladi.fragments.EditPlace
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.ArrayList


class Description : Fragment(),AdapterView.OnItemSelectedListener{

    private lateinit var descriptionBinding: FragmentDescriptionBinding
    private lateinit var utils : Utils
    private var priceType = arrayOf("per Match","per Hour","per Day")
    private var selectedPriceType = String()
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
    private var list = ArrayList<String>()
    private var otherTiming = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        descriptionBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_description, container, false)
        utils = Utils(context!!)
        descriptionBinding.mondayTiming.setOnClickListener {
            openClock("monday","start")
            openClock("monday","end")
        }
        descriptionBinding.tuesdayTiming.setOnClickListener {
            openClock("tuesday","start")
            openClock("tuesday","end")
        }
        descriptionBinding.wednesdayTiming.setOnClickListener {
            openClock("wednesday","start")
            openClock("wednesday","end")
        }
        descriptionBinding.thursdayTiming.setOnClickListener {
            openClock("thursday","start")
            openClock("thursday","end")
        }
        descriptionBinding.fridayTiming.setOnClickListener {
            openClock("friday","start")
            openClock("friday","end")
        }
        descriptionBinding.saturdayTiming.setOnClickListener {
            openClock("saturday","start")
            openClock("saturday","end")
        }
        descriptionBinding.sundayTiming.setOnClickListener {
            openClock("sunday","start")
            openClock("sunday","end")
        }

        getPriceType()
        descriptionBinding.nextButton.setOnClickListener {
            if(validateData()){
                navigateToCategory()
            }
        }

        descriptionBinding.otherTimings.setOnCheckedChangeListener { _, p1 ->
            otherTiming = p1
        }

        return descriptionBinding.root
    }


    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        selectedPriceType = priceType[p2]
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
                            descriptionBinding.mondayTiming.text = mondayStartTiming.plus(" - ").plus(mondayEndTiming)
                        }
                        "tuesday" -> {
                            val endTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                            tuesdayEndTiming = endTime.toString()
                            descriptionBinding.tuesdayTiming.text = tuesdayStartTiming.plus(" - ").plus(tuesdayEndTiming)
                        }
                        "wednesday" ->{
                            val endTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                            wednesdayEndTiming = endTime.toString()
                            descriptionBinding.wednesdayTiming.text = wednesdayStartTiming.plus(" - ").plus(wednesdayEndTiming)
                        }
                        "thursday" -> {
                            val endTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                            thursdayEndTiming = endTime.toString()
                            descriptionBinding.thursdayTiming.text = thursdayStartTiming.plus(" - ").plus(thursdayEndTiming)
                        }
                        "friday" -> {
                            val endTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                            fridayEndTiming = endTime.toString()
                            descriptionBinding.fridayTiming.text = fridayStartTiming.plus(" - ").plus(fridayEndTiming)
                        }
                        "saturday" ->{
                            val endTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                            saturdayEndTiming = endTime.toString()
                            descriptionBinding.saturdayTiming.text = saturdayStartTiming.plus(" - ").plus(saturdayEndTiming)
                        }
                        "sunday" ->{
                            val endTime = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                            sundayEndTiming = endTime.toString()
                            descriptionBinding.sundayTiming.text = sundayStartTiming.plus(" - ").plus(sundayEndTiming)
                        }
                    }
                }

            }, hourOfDay, minute, false)

        if(type == "end") mTimePicker.setTitle("Select start Time")
        else if(type == "start")  mTimePicker.setTitle("Select end Time")
        mTimePicker.show()


    }



    private fun navigateToCategory() {
        Toast.makeText(context,"Updating",Toast.LENGTH_SHORT).show()
        list.add(mondayStartTiming.plus(" - ").plus(mondayEndTiming))
        list.add(tuesdayStartTiming.plus(" - ").plus(tuesdayEndTiming))
        list.add(wednesdayStartTiming.plus(" - ").plus(wednesdayEndTiming))
        list.add(thursdayStartTiming.plus(" - ").plus(thursdayEndTiming))
        list.add(fridayStartTiming.plus(" - ").plus(fridayEndTiming))
        list.add(saturdayStartTiming.plus(" - ").plus(saturdayEndTiming))
        list.add(sundayStartTiming.plus(" - ").plus(sundayEndTiming))

        val bundle = Bundle()
        bundle.putString("locality",arguments?.getString("locality").toString())
        bundle.putString("country",arguments?.getString("country").toString())
        bundle.putStringArrayList("photosString",arguments?.getStringArrayList("photosString"))
        bundle.putString("description",descriptionBinding.description.text.toString())
        bundle.putString("title",descriptionBinding.title.text.toString())
        bundle.putString("price",descriptionBinding.price.text.toString())
        bundle.putString("priceType",selectedPriceType)
        bundle.putString("city",arguments?.getString("city").toString())
        bundle.putString("latitude",arguments?.getString("latitude").toString())
        bundle.putString("longitude",arguments?.getString("longitude").toString())
        bundle.putString("map",arguments?.getString("map").toString())
        bundle.putString("postalCode",arguments?.getString("postalCode").toString())
        bundle.putStringArrayList("timings",list)
        bundle.putBoolean("otherTiming",otherTiming)
        findNavController().navigate(R.id.action_description2_to_selectCategory2,bundle)
    }


    private fun validateData(): Boolean {
        var validate = true
        if(mondayStartTiming.isEmpty() || mondayEndTiming.isEmpty()
            || tuesdayStartTiming.isEmpty() || tuesdayEndTiming.isEmpty()
            || wednesdayStartTiming.isEmpty() || wednesdayEndTiming.isEmpty()
            || thursdayStartTiming.isEmpty() || thursdayEndTiming.isEmpty()
            || fridayStartTiming.isEmpty() || fridayEndTiming.isEmpty()
            || saturdayStartTiming.isEmpty() || saturdayEndTiming.isEmpty()
            || sundayStartTiming.isEmpty() || sundayEndTiming.isEmpty()){
            descriptionBinding.timings.startAnimation(AnimationUtils.loadAnimation(context,R.anim.shake))
            validate = false
        }
        if(descriptionBinding.title.text.isEmpty()){
            descriptionBinding.title.startAnimation(AnimationUtils.loadAnimation(context,R.anim.shake))
            validate = false
        }
        if(descriptionBinding.description.text.isEmpty()){
            descriptionBinding.descLayout.startAnimation(AnimationUtils.loadAnimation(context,R.anim.shake))
            validate = false
        }
        if(descriptionBinding.price.text.isEmpty()){
            descriptionBinding.price.startAnimation(AnimationUtils.loadAnimation(context,R.anim.shake))
            validate = false
        }

        if(validate) return true
        return false
    }

    private fun getPriceType() {
        descriptionBinding.priceSpinner.onItemSelectedListener = this
        val aa = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, priceType)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        descriptionBinding.priceSpinner.adapter = aa
    }



}
