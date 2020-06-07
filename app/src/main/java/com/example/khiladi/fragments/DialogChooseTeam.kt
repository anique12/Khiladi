package com.example.khiladi.fragments


import com.example.khiladi.Adapters.ChooseTeamAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.khiladi.Models.Event
import com.example.khiladi.Models.Team
import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentDialogChooseTeamBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList

class DialogChooseTeam(var respondedEvent: Event) : BottomSheetDialogFragment() {

    lateinit var  dialogChooseTeamBinding: FragmentDialogChooseTeamBinding
    private var currentUser = FirebaseAuth.getInstance().currentUser
    private var selectedTeam = Team()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogChooseTeamBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_dialog_choose_team,container,false)
        val list = ArrayList<Team>()
           FirebaseDatabase.getInstance().getReference("khiladi/${currentUser?.uid}/teams/${respondedEvent.category}/").addValueEventListener(object : ValueEventListener{
               override fun onCancelled(p0: DatabaseError) {

               }

               override fun onDataChange(p0: DataSnapshot) {
                   p0.children.forEach{
                       val id = it.value.toString()
                       FirebaseDatabase.getInstance().getReference("teams/${respondedEvent.category}/$id").addValueEventListener(object : ValueEventListener{
                           override fun onDataChange(p0: DataSnapshot) {
                               val team = p0.getValue(Team::class.java)
                               if(team?.captainId == currentUser?.uid){
                                   list.add(team!!)
                               }
                               dialogChooseTeamBinding.chooseTeamRecyclerView.adapter = ChooseTeamAdapter(list,null,this@DialogChooseTeam)
                           }

                           override fun onCancelled(p0: DatabaseError) {
                           }

                       })
                   }
               }

           })

        return dialogChooseTeamBinding.root
    }

}
