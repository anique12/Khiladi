package com.example.khiladi.fragments


import com.example.khiladi.Adapters.CondenseKhiladiAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.Models.Notification
import com.example.khiladi.Models.Team
import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentTeamProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

/**
 * A simple [Fragment] subclass.
 */
class TeamProfile : Fragment(), CondenseKhiladiAdapter.ChoosePlayersCallBack,
    CondenseKhiladiAdapter.FanListnerer {


    override fun addFan(khiladi: Khiladi, textView: TextView) {
        val hashmap = HashMap<String,String>()
        hashmap[currentUser?.uid.toString()] = currentUser?.uid.toString()
        firebaseDatabase.getReference("khiladi/${khiladi.uid}/fans").setValue(hashmap).addOnSuccessListener {
            textView.background = resources.getDrawable(R.drawable.respond_unfocus)
            Toast.makeText(context,"You are now fan of "+khiladi.name, Toast.LENGTH_SHORT).show()

        }
    }

    override fun callBack(khiladi: Khiladi) {
        selectedKhiladi = khiladi
        val bundle = Bundle()
        bundle.putParcelable("khiladi",selectedKhiladi)
        findNavController().navigate(R.id.action_teamProfile_to_khiladiProfile,bundle)
    }

    private lateinit var fragmentTeamProfileBinding: FragmentTeamProfileBinding
    private var selectedKhiladi = Khiladi()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentTeamProfileBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_team_profile,container,false)
        if(arguments?.getParcelable<Team>("team") != null){
            val team = arguments?.getParcelable<Team>("team")
            loadDataIntoFirebase(team)
        }
        if(arguments?.getParcelable<Notification>("notification") != null){
            if(arguments?.getParcelable<Notification>("notification")!!.accept == false){
                fragmentTeamProfileBinding.request.visibility = View.VISIBLE
            }
            getDataAndUpdateUI()
        }

        return fragmentTeamProfileBinding.root
    }

    private fun getDataAndUpdateUI() {
        val notification =  arguments?.getParcelable<Notification>("notification")
        val teamId = notification?.teamId
        val category = notification?.category
        firebaseDatabase.getReference("teams/$category/$teamId").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val team = p0.getValue(Team::class.java)
                loadDataIntoFirebase(team)
            }

        })
    }

    private fun loadDataIntoFirebase(team: Team?) {
        fragmentTeamProfileBinding.descriptionTeam.text = team?.description
        fragmentTeamProfileBinding.nameTeam.text = team?.name
        val playersList = ArrayList<String>()
        Picasso.get().load(team?.profile).into(fragmentTeamProfileBinding.imageViewTeamProfile)
        for(i in team?.players!!){
            playersList.add(i.value)
        }
        val players = ArrayList<Khiladi>()
        for (i in playersList){
            FirebaseDatabase.getInstance().getReference("khiladi/$i").addValueEventListener(object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                   // players.clear()
                    players.add(p0.getValue(Khiladi::class.java)!!)
                    fragmentTeamProfileBinding.recyclerViewPlayersTeamProfile.adapter = CondenseKhiladiAdapter(players,null,this@TeamProfile)
                }
            })
        }
    }



}
