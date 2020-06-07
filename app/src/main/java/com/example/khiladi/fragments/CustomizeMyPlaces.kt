package com.example.khiladi.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentCustomizeMyPlacesBinding
import java.text.DateFormat
import java.util.*
import java.text.SimpleDateFormat








class CustomizeMyPlaces : Fragment() {

    private lateinit var customizeMyPlacesBinding: FragmentCustomizeMyPlacesBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        customizeMyPlacesBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_customize_my_places, container, false)
        val date = DateFormat.getDateInstance().format(Date())


        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy",Locale.getDefault())
        val day1 = Calendar.getInstance()
        val day2 = Calendar.getInstance()
        val day3 = Calendar.getInstance()
        val day4 = Calendar.getInstance()
        val day5 = Calendar.getInstance()
        val day6 = Calendar.getInstance()
        val day7 = Calendar.getInstance()

        day1.add(Calendar.DAY_OF_WEEK,0)
        day2.add(Calendar.DAY_OF_WEEK,1)
        day3.add(Calendar.DAY_OF_WEEK,2)
        day4.add(Calendar.DAY_OF_WEEK,3)
        day5.add(Calendar.DAY_OF_WEEK,4)
        day6.add(Calendar.DAY_OF_WEEK,5)
        day7.add(Calendar.DAY_OF_WEEK,6)

        val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val day = SimpleDateFormat("EEEE",Locale.getDefault()).format(Date())
        val sdf = SimpleDateFormat("EEEE",Locale.getDefault())
        val first = sdf.format(day1.time)
        val second = sdf.format(day2.time)
        val third = sdf.format(day3.time)
        val fourth = sdf.format(day4.time)
        val fifth = sdf.format(day5.time)
        val six = sdf.format(day6.time)
        val seven = sdf.format(day7.time)

        customizeMyPlacesBinding.day1.text = first
        customizeMyPlacesBinding.day2.text = second
        customizeMyPlacesBinding.day3.text = third
        customizeMyPlacesBinding.day4.text = fourth
        customizeMyPlacesBinding.day5.text = fifth
        customizeMyPlacesBinding.day6.text = six
        customizeMyPlacesBinding.day7.text = seven
        return customizeMyPlacesBinding.root
    }


}
