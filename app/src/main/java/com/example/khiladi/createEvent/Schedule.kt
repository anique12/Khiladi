package com.example.khiladi.createEvent


import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Models.Event
import com.example.khiladi.R
import com.example.khiladi.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
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
    private var place = String()
    private var placeRef = String()
    private var firebaseDatabase = FirebaseDatabase.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        scheduleView =  inflater.inflate(R.layout.fragment_schedule, container, false)

        scheduleView.selectTime.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            var hourOfDay = mcurrentTime.get(Calendar.HOUR_OF_DAY)
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
                    place = arguments?.getString("place").toString()
                    placeRef = arguments?.getString("placeRef").toString()
                    checkPlaceAvailability()
                    view?.timeTextView?.visibility = View.VISIBLE
                    view?.post?.isEnabled = true
                    time = "$selectedHour:$mm_precede$selectedMinute$AM_PM"
                    view?.timeTextView?.text = time
                }, hourOfDay, minute, false)

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
               // Toast.makeText(context, datee, Toast.LENGTH_SHORT).show()
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
        scheduleView.post.setOnClickListener {
            findNavController().navigate(R.id.action_schedule_to_home)
            val utils = Utils(context!!)
            utils.showLoading()
            val description = scheduleView.eventDesc.text.toString()
            val teamId = arguments?.getString("team").toString()
            val sports = arguments?.getString("sports").toString()
            val tsLong = System.currentTimeMillis()
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

    private fun checkPlaceAvailability() {
        FirebaseDatabase.getInstance().getReference("$placeRef/timings").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

            }

        })
    }


}
