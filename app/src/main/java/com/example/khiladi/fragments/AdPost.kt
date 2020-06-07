package com.example.khiladi.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.khiladi.DialogChooseSingleSportsCategory
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.databinding.FragmentAdPostBinding
import SportsSingleCategoriesAdapter
import android.app.Activity.RESULT_OK
import android.net.Uri
import android.util.Log
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.R
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList


class AdPost : Fragment(), SportsSingleCategoriesAdapter.SportsSingleCategoryListener {

    override fun selectedCategory(selectedSportsCategory: SportsCatergory) {

    }

    override fun sportsCategoryListener(sportsCategory: ArrayList<SportsCatergory>) {
        sports = sportsCategory
        Toast.makeText(context,sports!![0].title,Toast.LENGTH_SHORT).show()
    }

    private lateinit var adPostBinding: FragmentAdPostBinding
    private var title= String()
    private var description = String()
    private var price = String()
    private var photos : ArrayList<Uri>? = ArrayList()
    private var sports : ArrayList<SportsCatergory>? = ArrayList()
    private var photosUrl : ArrayList<String> = ArrayList()
    var khiladi = Khiladi()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        adPostBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ad_post,container,false)

        adPostBinding.imageBtnAdpost.setOnClickListener {
           openGallery()
        }
        adPostBinding.selectSportsAdpost.setOnClickListener {
            dialogFragment()
        }

        adPostBinding.nextBtnAdpost.setOnClickListener {
            if (validatePicture() && validateTitle() && validateprice() && validateDescription() && validateSports() && validatePicture()){
                startAnimation()
                storePicsToFirebase()
            }
            else{
                Toast.makeText(context,"Not Done",Toast.LENGTH_SHORT).show()
                validateDescription()
                validatePicture()
                validateSports()
                validateTitle()
                validateprice()

            }
        }

        return adPostBinding.root
    }

    private fun storePicsToFirebase() {
        photos?.forEach {
            val filename = UUID.randomUUID().toString()
            val firebaseStorage = FirebaseStorage.getInstance().getReference("Ads/$filename")
            firebaseStorage.putFile(it).addOnSuccessListener {
                firebaseStorage.downloadUrl.addOnSuccessListener {
                    photosUrl.add(it.toString())
                    if (photos?.size == photosUrl.size){
                        Toast.makeText(context,"Pics uploaded",Toast.LENGTH_SHORT).show()
                        stopAnimation()
                        Toast.makeText(context,photosUrl.toString(),Toast.LENGTH_SHORT).show()
                        Log.d("photoURrl",photosUrl.toString())
                       // addDataToFirebase()
                    }
                }.addOnFailureListener{
                    Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                    stopAnimation()
                    return@addOnFailureListener
                }
            }.addOnFailureListener{
                stopAnimation()
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                return@addOnFailureListener
            }
        }
    }

/*
    private fun addDataToFirebase() {
        val uid = UUID.randomUUID().toString()
        val current = FirebaseAuth.getInstance().currentUser?.uid
        val khiladiref = FirebaseDatabase.getInstance().getReference("khiladi/$current")
        khiladiref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                 khiladi = p0.getValue(Khiladi::class.java)!!
                 val ads = Ads2(uid,khiladi,title,description,price,photosUrl,Sports!![0])
                val firebaseDatabase =  FirebaseDatabase.getInstance().getReference("/ads/$uid")
                firebaseDatabase.setValue(ads).addOnSuccessListener {
                    stopAnimation()
                    Toast.makeText(context,"Your ad has been uploaded successfully",Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_adPost_to_ads)
                }.addOnFailureListener {
                    stopAnimation()
                    Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_adPost_to_ads)
                }
            }

        })

    }
*/

    private fun validateSports(): Boolean {
        if (sports ==null){
            Toast.makeText(context,"Please add Sports category",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validateDescription(): Boolean {
        val _description = adPostBinding.descriptionAdpost.text.toString()
        if (_description.isEmpty()){
            adPostBinding.descriptionAdpost.error = "Please enter description of post"
            return false
        }
        description = _description
        return true
    }


    private fun dialogFragment() {
        val fragment = DialogChooseSingleSportsCategory()
        fragment.setTargetFragment(this,1)
        fragment.show(fragmentManager!!, "")
    }

    private fun validateprice(): Boolean {
        var _price = adPostBinding.PriceAdPost.text.toString()
        if(_price.isEmpty()){
            adPostBinding.PriceAdPost.error = "Please enter price"
            return false
        }
        price = _price
        return true
    }

    private fun validatePicture(): Boolean {
        if (photos==null){
            Toast.makeText(context,"Please add atleast one photo",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


    private fun openGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
        startActivityForResult(intent, 101)
    }

    private fun startAnimation(){
        adPostBinding.nextBtnAdpost.startAnimation()
    }

    private fun stopAnimation(){
        adPostBinding.nextBtnAdpost.stopAnimation()
        adPostBinding.nextBtnAdpost.revertAnimation()
    }

    private fun validateTitle(): Boolean {
         val _title = adPostBinding.titleAdpost.text.toString()
        if (_title.isEmpty()) {
            adPostBinding.titleAdpost.error = "Please enter a title"
            return false
        }
        title = _title
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

             if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            val clip = data.clipData
            if (clip == null)
            else{
                for (i in 0 until clip.itemCount) {
                    val item = clip.getItemAt(i)
                    val uri = item.uri
                    photos?.add(uri)
                }
            }


        }
        }
    }

