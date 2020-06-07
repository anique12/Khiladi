package com.example.khiladi.createEvent


import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.khiladi.Models.Event

import com.example.khiladi.R
import com.example.khiladi.Utils
import com.example.khiladi.databinding.FragmentScheduleBinding
import com.google.firebase.database.FirebaseDatabase
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.fragment_create_event.view.*
import kotlinx.android.synthetic.main.fragment_schedule.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class Schedule : Fragment() {

    private lateinit var scheduleView: View
    var datee : String? = null
    var time : String? = null
    private lateinit var horizontalCalendar : HorizontalCalendar
    private var dateNego = false
    private var timeNego = false
    private var placeNego = false
    private var urgent = false
    private var firebaseDatabase = FirebaseDatabase.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        scheduleView =  inflater.inflate(R.layout.fragment_schedule, container, false)

        scheduleView.selectTime.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    time = "$selectedHour:$selectedMinute"
                    scheduleView.timeTextView.text = time
                }, hour, minute, false)
            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }

        val startDate = Calendar.getInstance()
        startDate.add(Calendar.DAY_OF_MONTH,0)

        /* end after 1 month from now */
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 1)

        horizontalCalendar = HorizontalCalendar.Builder(scheduleView, R.id.calender)
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
                Toast.makeText(context, datee, Toast.LENGTH_SHORT).show()
            }

        }


        scheduleView.dateNego.setOnCheckedChangeListener { _, p1 ->
            dateNego = p1
        }

        scheduleView.timeNego.setOnCheckedChangeListener { _, p1 ->
            timeNego = p1
        }

        scheduleView.placeNego.setOnCheckedChangeListener { _, p1 ->
            placeNego = p1
        }

        scheduleView.urgent.setOnCheckedChangeListener { _, p1 ->
            urgent = p1
        }
        scheduleView.doneButton.setOnClickListener {
            val utils = Utils(context!!)
            utils.showLoading()
            val description = scheduleView.desc.text.toString()
            val teamId = arguments?.getString("team").toString()
            val sports = arguments?.getString("sports").toString()
            val tsLong = System.currentTimeMillis()
            val place = arguments?.getString("place").toString()
            val placeRef = arguments?.getString("placeRef").toString()
            val ref = firebaseDatabase.getReference("events/$sports/$teamId").push()
            val id = ref.key
            val event = Event(id!!,teamId,null,tsLong.toString(),datee,time,urgent,null,sports,place,null,timeNego,dateNego,
                placeNego,description,placeRef)
            ref.setValue(event).addOnSuccessListener {
                utils.hideLoading()
                Toast.makeText(context,"Event created",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                utils.hideLoading()
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
            }

        }
        return scheduleView
    }


}
