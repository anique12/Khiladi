package com.example.khiladi.fragments

import SportsSingleCategoriesAdapter
import com.example.khiladi.Adapters.TeamAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.khiladi.DialogChooseSingleSportsCategory
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.Models.Team
import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentTeamsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.choose_category_bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_teams.view.*
import kotlin.collections.ArrayList

class Teams : Fragment(), TeamAdapter.TeamListener ,
    SportsSingleCategoriesAdapter.SportsSingleCategoryListener {

    override fun selectedCategory(selectedSportsCategory: SportsCatergory) {
        selectedCategory = selectedSportsCategory
        showAllTeams()
        teamView.firstView.text = selectedCategory.title
        Picasso.get().load(selectedCategory.photo).into(teamView.imageView)
        collapseBottomSheet()
    }

    private fun collapseBottomSheet() {
        val behavior = BottomSheetBehavior.from(teamView.chooseCategorySheet)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun sportsCategoryListener(sportsCategory: ArrayList<SportsCatergory>) {
        selectedCategory = sportsCategory[0]
        showAllTeams()
        teamView.allTeams.background = resources.getDrawable(R.drawable.chips)
        teamView.myTeams.background = resources.getDrawable(R.drawable.chips_unfocus)
    }

    override fun callback(team: Team) {
        selectedTeam = team
        val bundle = Bundle()
        bundle.putParcelable("team",team)
        findNavController().navigate(R.id.action_teams_to_teamProfile,bundle)
    }

    private lateinit var teamView : View
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    var myteams = ArrayList<Team>()
    var allTeams = ArrayList<Team>()
    private var selectedTeam = Team()
    private var currentUser = FirebaseAuth.getInstance().currentUser
    private var selectedCategory = SportsCatergory("-M28tl8D-yXJYjhiMHOX","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcS3-KtExMO-J4Ug7vRkfAja4G95a5JI1_bHXzGsJTva0fSMd2ni","cricket")




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        teamView = inflater.inflate(R.layout.fragment_teams, container, false)
        showAllTeams()
        inflateBottomSheet()
        teamView.allTeams.setOnClickListener {
            teamView.allTeams.background = resources.getDrawable(R.drawable.chips)
            teamView.myTeams.background = resources.getDrawable(R.drawable.chips_unfocus)
            showAllTeams()
        }
        teamView.myTeams.setOnClickListener {
            teamView.allTeams.background = resources.getDrawable(R.drawable.chips_unfocus)
            teamView.myTeams.background = resources.getDrawable(R.drawable.chips)
            showMyTeams()
        }
        teamView.newTeams.setOnClickListener {
            findNavController().navigate(R.id.action_teams_to_createTeam)
        }

        return teamView
    }

    private fun dialogFragment() {
        val fragment = DialogChooseSingleSportsCategory()
        fragment.setTargetFragment(this,1)
        fragment.show(fragmentManager!!, "")
    }

    private fun showAllTeams() {
        allTeams.clear()
        teamView.recyclerViewTeams.adapter = TeamAdapter(allTeams,this,null)
        firebaseDatabase.getReference("teams/${selectedCategory.title}/").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                            val team = it.getValue(Team::class.java)
                            allTeams.add(team!!)
                }
                teamView.recyclerViewTeams.adapter = TeamAdapter(allTeams,this@Teams,null)
            }
        })
    }

    private var list = ArrayList<SportsCatergory>()
    fun inflateBottomSheet(){
        teamView.firstView.text = selectedCategory.title
        Picasso.get().load(selectedCategory.photo).into(teamView.imageView)
        FirebaseDatabase.getInstance().getReference("categories").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                p0.children.forEach{
                    val sportsCatergory = it.getValue(SportsCatergory::class.java)
                    list.add(sportsCatergory!!)
                    teamView.recyclerViewCategoriesSports.adapter = SportsSingleCategoriesAdapter(context!!,list,null,this@Teams)
                }
            }

        })
        val manager = StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        teamView.recyclerViewCategoriesSports.layoutManager = manager
    }

    private fun showMyTeams() {

        myteams.clear()
        teamView.recyclerViewTeams.adapter = TeamAdapter(myteams,this@Teams,null)
        firebaseDatabase.getReference("khiladi/${currentUser?.uid}/teams/${selectedCategory.title}/").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                var myTeamsString  = String()
                p0.children.forEach{
                            myTeamsString = it.value.toString()
                            firebaseDatabase.getReference("teams/${selectedCategory.title}/$myTeamsString").addValueEventListener(object : ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {}

                            override fun onDataChange(p0: DataSnapshot) {
                                val team = p0.getValue(Team::class.java)
                                if(team?.captainId == currentUser?.uid){
                                    myteams.add(p0.getValue(Team::class.java)!!)
                                }
                                teamView.recyclerViewTeams.adapter = TeamAdapter(myteams,this@Teams,null)
                            }

                        })



                }

            }
        })
    }

}
