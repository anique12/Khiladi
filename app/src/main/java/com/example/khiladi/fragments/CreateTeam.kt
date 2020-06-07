package com.example.khiladi.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentCreateTeamBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import CondenseKhiladiAddPlayerAdapter
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.navigation.fragment.findNavController
import com.example.khiladi.DialogChooseSingleSportsCategory
import com.example.khiladi.Models.*
import com.example.khiladi.Models.Notification
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_create_team.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@Suppress("DEPRECATION")
class CreateTeam : Fragment(),CondenseKhiladiAddPlayerAdapter.ChoosePlayersCallBack,SportsSingleCategoriesAdapter.SportsSingleCategoryListener{
    override fun selectedCategory(selectedSportsCategory: SportsCatergory) {

    }

    override fun sportsCategoryListener(sportsCategory: ArrayList<SportsCatergory>) {
            selectedSport = sportsCategory[0]
    }


    private lateinit var createTeamBinding: FragmentCreateTeamBinding
    var khiladilist: ArrayList<Khiladi>? =  ArrayList()
    var requestedTeamPlayers : ArrayList<Khiladi>? = ArrayList()
    var requestedTeamPlayersId : ArrayList<String>? = ArrayList()
    private var selectedSport : SportsCatergory? = null
    var byteArray = ArrayList<ByteArray>()
    var downloadedUrl = String()
    var currentUser = FirebaseAuth.getInstance().currentUser
    var key : String? =null


    override fun callBack(khiladi: ArrayList<Khiladi>) {
        requestedTeamPlayers = khiladi
        createTeamBinding.dialogChoosePlayers.visibility = View.GONE
        createTeamBinding.backgroundLayout.visibility = View.VISIBLE

    }

    private fun sendNotification() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserPhoto = currentUser?.photoUrl.toString()
        val tsLong = System.currentTimeMillis()
        val description = " send you a request to join"

        for (i in requestedTeamPlayers!!) {
            val firebaseDatabase = FirebaseDatabase.getInstance().getReference("notification/${i.uid}").push()
            val id = firebaseDatabase.key
            val notification = Notification(id,currentUserPhoto, tsLong, description, i.uid, currentUser?.uid!!,"requestJoinTeam",selectedSport?.title, key!!,false)
            firebaseDatabase.setValue(notification).addOnSuccessListener {
              //  Toast.makeText(context, "Requests send to your players", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        createTeamBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_create_team,container,false)

        createTeamBinding.playersCreateTeam.setOnClickListener {
            if(selectedSport == null){
                Toast.makeText(context,"Select category first",Toast.LENGTH_SHORT).show()
            }else{
                createTeamBinding.dialogChoosePlayers.visibility = View.VISIBLE
                createTeamBinding.backgroundLayout.visibility = View.GONE
                dialog()
            }

        }
        createTeamBinding.nextBtnCreateTeam.setOnClickListener {
            if(validateTitle() && validateSports() && pickPlayers() && validatePic()){
                Toast.makeText(context,"done",Toast.LENGTH_SHORT).show()
                storage()
            }
            else{
                validateSports()
                validateTitle()
                pickPlayers()
            }
        }


        createTeamBinding.imageViewCreateTeam.setOnClickListener {
            openGallery()

        }
        createTeamBinding.selectSportsCreateTeam.setOnClickListener {
            dialogFragment()

        }
        return createTeamBinding.root
    }

    private fun validatePic(): Boolean {
        if(byteArray.isEmpty()){
            Toast.makeText(context,"Please select profile pic",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun storage() {
        createTeamBinding.nextBtnCreateTeam.startAnimation()
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("teams/${currentUser?.uid}/$filename")
        ref.putBytes(byteArray[0]).addOnSuccessListener {
            Toast.makeText(context,"uploaded",Toast.LENGTH_SHORT).show()
            ref.downloadUrl.addOnSuccessListener {
                downloadedUrl = it.toString()
                addToFirebase()
            }.addOnFailureListener {
                Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
                createTeamBinding.nextBtnCreateTeam.stopAnimation()
            }
        }.addOnFailureListener{
            Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
            createTeamBinding.nextBtnCreateTeam.stopAnimation()
        }

    }

    private fun addToFirebase() {
        for (i in requestedTeamPlayers!!){
            requestedTeamPlayersId?.add(i.uid)
        }
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        val hashMap = HashMap<String,String>()
        val ref = FirebaseDatabase.getInstance().getReference("teams/${selectedSport?.title}").push()
        hashMap.set(FirebaseAuth.getInstance().currentUser?.uid.toString(),FirebaseAuth.getInstance().currentUser?.uid.toString())
        key = ref.key.toString()
        val team = Team(key,downloadedUrl,titleCreateTeam.text.toString(),selectedSport?.id!!,"Open",descriptionCreateTeam.text.toString(),hashMap,ts,currentUser?.uid)
        ref.setValue(team).addOnSuccessListener {
            createTeamBinding.nextBtnCreateTeam.stopAnimation()
            Toast.makeText(context,"Team created",Toast.LENGTH_LONG).show()
            val team = HashMap<String,String>()
            team[key.toString()] = key.toString()
            FirebaseDatabase.getInstance().getReference("khiladi/${currentUser?.uid}/teams/${selectedSport?.title}/").updateChildren(team as Map<String, Any>).addOnSuccessListener {
                sendNotification()
                findNavController().navigate(R.id.action_createTeam_to_teams)
            }

        }.addOnFailureListener {
            createTeamBinding.nextBtnCreateTeam.stopAnimation()
            Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
        }
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK && data != null) {
            val string = data.data.toString()
            val profileImageUri = Uri.parse(string)
            val bmp = MediaStore.Images.Media.getBitmap(context?.contentResolver, profileImageUri)
            val baos = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos)
            byteArray.add(baos.toByteArray())
            createTeamBinding.imageViewCreateTeam.setImageURI(profileImageUri)
        }
    }

    private fun validateSports(): Boolean {
        if(selectedSport == null){
           Toast.makeText(context,"Please select your team Sports",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun dialogFragment() {
        val fragment = DialogChooseSingleSportsCategory()
        fragment.setTargetFragment(this,1)
        fragment.show(fragmentManager!!, "")
    }

    private fun pickPlayers(): Boolean {
        if (requestedTeamPlayers == null){
            Toast.makeText(context,"Please select players for your team",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validateTitle(): Boolean {
        if(titleCreateTeam.text.isEmpty()){
            titleCreateTeam.error = "Please select title for your team"
            return false
        }
        return true
    }

    private fun dialog() {
        khiladilist = ArrayList()
        FirebaseDatabase.getInstance().getReference("users/${selectedSport?.title}").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                    FirebaseDatabase.getInstance().getReference("khiladi/${it.value.toString()}").addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if(p0.exists()){
                                val khiladi = p0.getValue(Khiladi::class.java)
                                khiladilist?.add(khiladi!!)
                                createTeamBinding.recyclerViewChoosePlayers.adapter = CondenseKhiladiAddPlayerAdapter(khiladilist!!,this@CreateTeam,createTeamBinding.doneBtndialogChoosePlayer)
                            }
                        }

                    })
                }
            }

        })

    }


}
