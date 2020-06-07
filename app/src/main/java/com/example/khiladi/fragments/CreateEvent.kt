package com.example.khiladi.fragments


import com.example.khiladi.Adapters.TeamAdapter
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.khiladi.DialogChooseSingleSportsCategory
import com.example.khiladi.Models.Event
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.Models.Team

import com.example.khiladi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.fragment_create_event.*
import kotlinx.android.synthetic.main.fragment_create_event.view.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class CreateEvent : Fragment(), TeamAdapter.TeamListener,SportsSingleCategoriesAdapter.SportsSingleCategoryListener {
    override fun selectedCategory(selectedSportsCategory: SportsCatergory) {

    }

    override fun sportsCategoryListener(sportsCategory: ArrayList<SportsCatergory>) {
        selectedSports = sportsCategory[0]
    }

    override fun callback(team: Team) {
        selectedTeam = team
        dialog.visibility = View.GONE
        Toast.makeText(context,selectedTeam.toString(),Toast.LENGTH_SHORT).show()
    }

    private lateinit var horizontalCalendar : HorizontalCalendar
    private var currentUser = FirebaseAuth.getInstance().currentUser
    private var teamList = ArrayList<Team>()
    private var selectedTeam : Team? = null
    var datee : String? = null
    var time : String? = null
    var selectedSports : SportsCatergory? = null
    private var urgent : Boolean? = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_event, container, false)
        view.selectCategory.setOnClickListener {
            dialogFragment()
        }

        view.chooseTime.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    time = "$selectedHour:$selectedMinute"
                    view.chooseTime.text = time
                }, hour, minute, false)//Yes 24 hour time
            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }
        val startDate = Calendar.getInstance()
        startDate.add(Calendar.DAY_OF_MONTH,0)

        /* end after 1 month from now */
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 1)

        horizontalCalendar = HorizontalCalendar.Builder(view, R.id.calendarView)
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

        view.urgentSwitch.setOnCheckedChangeListener { p0, p1 ->
            if(p1){
                view.chooseTime.visibility = View.GONE
                view.calendarView.visibility = View.GONE
                urgent = true
            }
            if(!p1){
                view.chooseTime.visibility = View.VISIBLE
                view.calendarView.visibility = View.VISIBLE
                urgent = false

            }
        }
        view.selectTeam.setOnClickListener {
            if(selectedSports == null){
                Toast.makeText(context,"Select your Sports first",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                dialog.visibility = View.VISIBLE
                FirebaseDatabase.getInstance().getReference("khiladi/${currentUser?.uid}/teams/${selectedSports?.title}").addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        teamList.clear()
                        view.recyclerViewTeam.adapter = TeamAdapter(teamList,null,this@CreateEvent)
                        p0.children.forEach{
                                val id = it.value.toString()
                                FirebaseDatabase.getInstance().getReference("teams/${selectedSports?.title}/$id").addValueEventListener(object :ValueEventListener{
                                    override fun onDataChange(p0: DataSnapshot) {
                                        val team = p0.getValue(Team::class.java)
                                        if(team?.captainId == currentUser?.uid ){
                                            teamList.add(team!!)
                                            view.recyclerViewTeam.adapter = TeamAdapter(teamList,null,this@CreateEvent)
                                        }
                                    }

                                    override fun onCancelled(p0: DatabaseError) {
                                    }

                                })
                            }
                    }

                })
            }

        }

        view.submit.setOnClickListener {

                if(selectedSports == null || selectedTeam == null || time == null || datee == null){
                    Toast.makeText(context,"Please enter all information",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else{
                    val ref = FirebaseDatabase.getInstance().getReference("events/${selectedSports?.title}/${selectedTeam?.id}").push()
                    val key = ref.key
                    val tsLong = System.currentTimeMillis() / 1000
                    val timestamp = tsLong.toString()
                    val event = Event(key!!,selectedTeam?.id,null,timestamp,datee,time,
                        urgent = false,
                        fixture = false,
                        category = selectedSports?.title,
                        place = "Rawalpindi",
                        responces = null
                    )
                    ref.setValue(event).addOnSuccessListener {
                        Toast.makeText(context,"Event created",Toast.LENGTH_SHORT).show()
                        view.findNavController().navigate(R.id.action_createEvent_to_home)
                    }.addOnFailureListener{
                        Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                    }
                }

            }
        return view
    }

    private fun dialogFragment() {
        val fragment = DialogChooseSingleSportsCategory()
        fragment.setTargetFragment(this,1)
        fragment.show(fragmentManager!!, "")
    }

}
