package com.example.khiladi.fragments.accountFragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.khiladi.fragments.DialogChooseSportsCategory
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentDetailedAccountInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList
import SportsCategoriesAdapter
import android.graphics.Bitmap
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import kotlin.collections.HashMap


@Suppress("DEPRECATION")
class DetailedAccountInfo : Fragment(),SportsCategoriesAdapter.SportsCategoryListener {

    private var profileImageUri = Uri.EMPTY
    private var chooseCategoryList : ArrayList<SportsCatergory>? = ArrayList()
    private lateinit var detailedAccountInfoBinding: FragmentDetailedAccountInfoBinding
    private var cityText= String()
    private var nameText = String()
    private var phoneText = String()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var currentUser = FirebaseAuth.getInstance().currentUser
    private var radioButtonId = String()
    private var gender = String()
    private var firebaseStorage = FirebaseStorage.getInstance()
    private var country = String()
    var byteArray = ArrayList<ByteArray>()


    override fun sportsCategoryListener(sportsCategory: ArrayList<SportsCatergory>) {
        chooseCategoryList = sportsCategory
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        detailedAccountInfoBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_detailed_account_info,container,false)

        profileImageUri = Uri.EMPTY

        detailedAccountInfoBinding.countryCodePickerDetailAccountFragment.registerPhoneNumberTextView(detailedAccountInfoBinding.phoneDetailAccountFragment)

        detailedAccountInfoBinding.imageBtnDetailAccountFragment.setOnClickListener {
            getPicture()
        }

        detailedAccountInfoBinding.selectSportsDetailAccountFragment.setOnClickListener {
            dialogFragment()
        }

        detailedAccountInfoBinding.nextBtnDetailAccountFragment.setOnClickListener {
            if (validateUsername() && validateCity() && validatePhone() && photoSelected() &&  sports() && validateRadioButton()){
                startAnimation()
                getGender()
                storePictureIntoFirebaseStorage()

            }
            else{
                validateCity()
                validatePhone()
                validateRadioButton()
                validateUsername()
                photoSelected()
                sports()
            }
        }
        return detailedAccountInfoBinding.root

    }

    private fun storePictureIntoFirebaseStorage() {
        val filename = UUID.randomUUID().toString()
        val ref = firebaseStorage.getReference("/profile/$filename")
        ref.putBytes(byteArray[0]).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                Toast.makeText(context,"pic uploaded",Toast.LENGTH_LONG).show()
                addKhiladiToFirebase(it)
            }
        }
    }

    private fun addKhiladiToFirebase(ref:Uri) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val firebaseChangeRequest = UserProfileChangeRequest.Builder().setPhotoUri(ref).setDisplayName(nameText).build()
        FirebaseAuth.getInstance().currentUser?.updateProfile(firebaseChangeRequest)
        val country = detailedAccountInfoBinding.countryCodePickerDetailAccountFragment.selectedCountryName
        val email = currentUser?.email.toString()
        val uriString = ref.toString()
        val hashMap = HashMap<String,String>()
        chooseCategoryList?.forEach {
            hashMap[it.id] = it.id
        }
        val khiladi = Khiladi(FirebaseAuth.getInstance().currentUser?.uid.toString(),nameText,email,uriString, cityText, country, gender, phoneText, hashMap)
        FirebaseAuth.getInstance().currentUser?.reload()
        firebaseDatabase.getReference("/khiladi/$uid").setValue(khiladi).addOnSuccessListener {
            chooseCategoryList?.forEach {
                val user = HashMap<String,String>()
                user[currentUser?.uid.toString()] = currentUser?.uid.toString()
                firebaseDatabase.getReference("users/${it.id}/").updateChildren(user as Map<String, Any>)
            }

            Toast.makeText(context, "updated", Toast.LENGTH_LONG).show()
            stopAnimation()
            //findNavController().navigate(R.id.action_detailedAccountInfo_to_more)
        }.addOnFailureListener {
            stopAnimation()
            Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
        }
    }



    private fun getGender() {
        detailedAccountInfoBinding.radioDetailAccountFragment.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                val rb = p0?.checkedRadioButtonId as RadioButton
                gender = rb.text.toString()
                Toast.makeText(context,gender,Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun dialogFragment() {
        val fragment = DialogChooseSportsCategory()
        fragment.setTargetFragment(this,1)
        fragment.show(fragmentManager!!, "")
    }

    private fun getPicture() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101)
    }

    private fun sports(): Boolean {
        if (chooseCategoryList==null){
            Toast.makeText(context,"Please select your Sports",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validateCity(): Boolean {
        val city = detailedAccountInfoBinding.cityDetailAccountFragment.text.toString()
        val cityEditText = detailedAccountInfoBinding.cityDetailAccountFragment
        if (city.isEmpty()) {
            cityEditText.error = "Please enter city name"
            return false
        }
        cityText = city
        return true
    }

    private fun validatePhone(): Boolean {
        val phoneEditText = detailedAccountInfoBinding.phoneDetailAccountFragment
        val countryCodePicker = detailedAccountInfoBinding.countryCodePickerDetailAccountFragment
        if(!countryCodePicker.isValid){
            phoneEditText.error = "Please enter phone number correctly"
            return false
        }
        phoneText = countryCodePicker.phoneNumber.toString()
        country = countryCodePicker.selectedCountryName
        return true
    }

    private fun validateUsername(): Boolean {
        val username = detailedAccountInfoBinding.usernameDetailAccountFragment.text.toString()
        val usernameEditText = detailedAccountInfoBinding.usernameDetailAccountFragment
        if (username.isEmpty()) {
            usernameEditText.error = "Please enter a username"
            return false
        }
        nameText = username
        return true
    }

    private fun photoSelected(): Boolean {
        if (profileImageUri == null) {
            Toast.makeText(context, "Please Add photo", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validateRadioButton():Boolean{
         var radioGroup = detailedAccountInfoBinding.radioDetailAccountFragment
         val id = radioGroup.checkedRadioButtonId
         if (id == -1){
             Toast.makeText(context,"Please select gender",Toast.LENGTH_SHORT).show()
             return false
         }
        radioButtonId = id.toString()
        return true
    }

    private fun startAnimation(){
        detailedAccountInfoBinding.nextBtnDetailAccountFragment.startAnimation()
    }

    private fun stopAnimation(){
        detailedAccountInfoBinding.nextBtnDetailAccountFragment.stopAnimation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            val string = data?.data.toString()
            profileImageUri = Uri.parse(string)
            detailedAccountInfoBinding.imageBtnDetailAccountFragment.setImageURI(profileImageUri)
            val bmp = MediaStore.Images.Media.getBitmap(context?.contentResolver, profileImageUri)
            val baos = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos)
            byteArray.add(baos.toByteArray())
        }

    }

}
