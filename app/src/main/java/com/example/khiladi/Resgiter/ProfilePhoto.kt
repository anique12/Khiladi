package com.example.khiladi.Resgiter


import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.Models.SportsCatergory

import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentProfilePhotoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.rilixtech.widget.countrycodepicker.Country
import kotlinx.android.synthetic.main.fragment_basic_info.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList


class ProfilePhoto : Fragment() {

    private lateinit var profilePhotoBinding: FragmentProfilePhotoBinding
    private var profileImageUri = Uri.EMPTY
    var byteArray = ArrayList<ByteArray>()
    private var firebaseStorage = FirebaseStorage.getInstance()
    lateinit var country: String
    lateinit var phoneNumber : String
    lateinit var gender : String
    lateinit var city : String
    lateinit var username : String
    var selectedPlayingSports = ArrayList<SportsCatergory>()
    var selectedInterestedSports = ArrayList<SportsCatergory>()
    private var firebaseAuth =FirebaseAuth.getInstance()
    private var currentUser = firebaseAuth.currentUser
    private var currentUserUid = currentUser?.uid
    private var firebaseDatabase = FirebaseDatabase.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        profilePhotoBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_profile_photo, container, false)
        profilePhotoBinding.skipButton.setOnClickListener {
            showLoading()
            getAllData()
            addKhiladiToFirebase(null)
        }

        profilePhotoBinding.previousButton.setOnClickListener {
            findNavController().navigate(R.id.action_profilePhoto_to_intrestedSports)
        }
        profilePhotoBinding.select.setOnClickListener {
            getPicture()
        }
        profilePhotoBinding.doneButton.setOnClickListener {
            getAllData()
            if (profileImageUri == Uri.EMPTY){
                Toast.makeText(context,"Select profile photo or skip button",Toast.LENGTH_SHORT).show()
            }else{
                showLoading()
                storePictureIntoFirebaseStorage()
            }
        }
        return profilePhotoBinding.root
    }

    private fun getAllData() {
        city = arguments?.getString("city").toString()
        gender = arguments?.getString("gender").toString()
        phoneNumber = arguments?.getString("phone").toString()
        country = arguments?.getString("country").toString()
        username = arguments?.getString("name").toString()
        selectedPlayingSports = arguments?.getParcelableArrayList<SportsCatergory>("playingSportsList")!!
        selectedInterestedSports = arguments?.getParcelableArrayList<SportsCatergory>("interestedSportsList")!!
    }

    private fun getPicture() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            val string = data?.data.toString()
            profileImageUri = Uri.parse(string)
            profilePhotoBinding.profilePhoto.setImageURI(profileImageUri)
            profilePhotoBinding.select.text = "Change"
            val bmp = MediaStore.Images.Media.getBitmap(context?.contentResolver, profileImageUri)
            val baos = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos)
            byteArray.add(baos.toByteArray())
        }

    }

    private fun storePictureIntoFirebaseStorage() {
        val filename = UUID.randomUUID().toString()
        val ref = firebaseStorage.getReference("/profile/$filename")
        ref.putBytes(byteArray[0]).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                addKhiladiToFirebase(it)
            }
        }.addOnFailureListener{
            Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
            hideLoading()
        }
    }

    private fun addKhiladiToFirebase(ref:Uri? = null) {
        val firebaseChangeRequest = UserProfileChangeRequest.Builder().setPhotoUri(ref).setDisplayName(username).build()
        FirebaseAuth.getInstance().currentUser?.updateProfile(firebaseChangeRequest)
        val email = currentUser?.email.toString()
        val uriString = ref.toString()
        val playingSportsHashmap = HashMap<String,String>()
        val interestedSportsHashmap = HashMap<String,String>()
        selectedPlayingSports.forEach {
            playingSportsHashmap[it.id] = it.id
        }
        selectedInterestedSports.forEach {
            interestedSportsHashmap[it.id] = it.id
        }
        val khiladi = Khiladi(currentUserUid!!,username,email,uriString,city, country, gender, phoneNumber, playingSportsHashmap,interestedSportsHashmap)
        FirebaseAuth.getInstance().currentUser?.reload()
        firebaseDatabase.getReference("/khiladi/$currentUserUid").setValue(khiladi).addOnSuccessListener {
            selectedPlayingSports.forEach {
                val user = HashMap<String,String>()
                user[currentUser?.uid.toString()] = currentUser?.uid.toString()
                firebaseDatabase.getReference("users/${it.title}/").updateChildren(user as Map<String, Any>)
            }
            Toast.makeText(context, "Updated", Toast.LENGTH_LONG).show()
            hideLoading()
            val fromUpdateProfile = Bundle()
            fromUpdateProfile.putBoolean("condition",true)
            fromUpdateProfile.putString("name",username)
            fromUpdateProfile.putString("photo",uriString)
            findNavController().navigate(R.id.action_profilePhoto_to_more,fromUpdateProfile)
        }.addOnFailureListener {
            hideLoading()
            Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
        }
    }

    private fun showLoading(){
        profilePhotoBinding.progressBar.visibility = View.VISIBLE
    }
    private fun hideLoading(){
        profilePhotoBinding.progressBar.visibility = View.GONE
    }
}
