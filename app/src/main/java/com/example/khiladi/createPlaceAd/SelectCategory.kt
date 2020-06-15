package com.example.khiladi.createPlaceAd


import SelectSportsCategoriesAdapter
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.khiladi.Models.Ads2
import com.example.khiladi.Models.Slot
import com.example.khiladi.Models.SportsCatergory

import com.example.khiladi.R
import com.example.khiladi.Utils
import com.example.khiladi.databinding.FragmentDialogChooseSportsCategoryBinding
import com.example.khiladi.databinding.FragmentSelectCategoryBinding
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.Serializable
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class SelectCategory : Fragment(),SelectSportsCategoriesAdapter.SelectedSingleCategoryListener  {

    private lateinit var selectedCategoryBinding: FragmentSelectCategoryBinding
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var currentUser = FirebaseAuth.getInstance().currentUser?.uid
    private var storage = FirebaseStorage.getInstance()
    private var categoryList = ArrayList<SportsCatergory>()
    private var selectedCategory : SportsCatergory? = null
    private var byteArray = ArrayList<ByteArray>()
    private var downloadedUrl = ArrayList<String>()
    var locality = String()
    var country = String()
    var description = String()
    var title = String()
    var price = String()
    var priceType = String()
    var latitude = String()
    var longitude = String()
    private var city = String()
    private var postalCode = String()
    var photoString = ArrayList<String>()
    private lateinit var utils : Utils
    private var mapSnapshot = String()
    private var timingList = ArrayList<String>()
    private var otherTimings = false
    private var fullList = ArrayList<ArrayList<Slot>>()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        selectedCategoryBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_select_category, container, false)
        inflateSports()
        utils = Utils(context!!)
        selectedCategoryBinding.nextButton.setOnClickListener {
            if(selectedCategory!=null){
                getAdArguments()
                storePicsToStorage()
            }
        }
        return selectedCategoryBinding.root
    }


    override fun sportsCategoryListener(sportsCategory: SportsCatergory) {
        selectedCategory = sportsCategory
        getAdArguments()
        storePicsToStorage()
    }

    private fun storePicsToStorage() {
        utils.showLoading()
        Log.d("photosString",photoString.toString())
        photoString.forEach {
            val uri = Uri.parse(it)
            val bmp = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
            val baos = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos)
            byteArray.add(baos.toByteArray())
        }
        Log.d("byteArray",byteArray.toString())
        byteArray.forEach {
            val filename = UUID.randomUUID().toString()
            val ref = storage.getReference("ads/$filename")
            ref.putBytes(it).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    downloadedUrl.add(it.toString())
                    Log.d("downloadedUrl",it.toString())
                    if(byteArray.size == downloadedUrl.size){
                        storeToFirebase()
                    }
                }
            }.addOnFailureListener{
                utils.hideLoading()
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun storeToFirebase() {
        val path = "ads/${selectedCategory?.title}/${country}/$city/${postalCode}/${currentUser}"
        val ref = firebaseDatabase.getReference(path)
        val key = ref.push().key
        val otherPath = path.plus("/$key")
        val photosHashMap = HashMap<String,String>()
        downloadedUrl.forEach {
            val uid = UUID.randomUUID().toString()
            photosHashMap.put(uid,it)
        }
        val ads = Ads2(key,currentUser,title,description,price,priceType,locality,latitude,longitude,photosHashMap,
            mapSnapshot,selectedCategory?.id,otherPath,fullList,otherTimings)
        ref.child(key!!).setValue(ads).addOnSuccessListener {
            storeIdToKhiladi(otherPath)
        }.addOnFailureListener {
            Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
            utils.hideLoading()
        }
    }


    private fun storeIdToKhiladi(path : String) {
        val hashMap = HashMap<String,String>()
        val name = UUID.randomUUID().toString()
        hashMap.put(name,path)
        firebaseDatabase.getReference("khiladi/$currentUser/ads").updateChildren(hashMap as Map<String, Any>).addOnSuccessListener {
            Toast.makeText(context,"UpLoaded",Toast.LENGTH_SHORT).show()
            utils.hideLoading()
            findNavController().navigate(R.id.action_selectCategory2_to_ads)
        }.addOnFailureListener {
            Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
            utils.hideLoading()
        }
    }



    private fun getAdArguments() {
        locality = arguments?.getString("locality").toString()
        country = arguments?.getString("country").toString()
        photoString = arguments?.getStringArrayList("photosString")!!
        title = arguments?.getString("title").toString()
        description = arguments?.getString("description").toString()
        price = arguments?.getString("price").toString()
        priceType = arguments?.getString("priceType").toString()
        longitude = arguments?.getString("longitude").toString()
        latitude = arguments?.getString("latitude").toString()
        postalCode = arguments?.getString("postalCode").toString()
        city = arguments?.getString("city").toString()
        mapSnapshot = arguments?.getString("map").toString()
        timingList = arguments?.getStringArrayList("timings")!!
        otherTimings = arguments?.getBoolean("otherTiming")!!
        timingList.forEach {
            val timeList = it.split("-")
            val startTime = getTimeIn24( timeList[0])
            val endTime = getTimeIn24( timeList[1])
            divideTimeInSlots(startTime, endTime,it)
        }
    }


    private fun divideTimeInSlots(startTime:Int,endTime:Int,t:String) {
        val slotList = ArrayList<Slot>()
        Toast.makeText(context,"dividing slots",Toast.LENGTH_SHORT).show()
        val slots = endTime - startTime
        Log.d("maslyYr",slots.toString())
        var time = startTime
        for(i in 0 until slots){
            val string : String = time.toString().plus(" - ").plus(time+1)
            slotList.add(Slot(string,false,t))
            time++
        }
        fullList.add(slotList)
    }

    private fun getTimeIn24(time : String): Int {

        return if(time.contains("PM")){
            val list = time.split(":")
            val newString = list[0].replace("\\s".toRegex(),"")
            val newint= newString.toInt()
            newint + 12
        } else {
            val list = time.split(":")
            val newString = list[0].replace("\\s".toRegex(),"")
            val newint= newString.toInt()
            newint
        }

    }

    private fun inflateSports() {
        selectedCategoryBinding.recyclerViewCategories.layoutManager = GridLayoutManager(context,3)
        firebaseDatabase.getReference("categories").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                categoryList.clear()
                p0.children.forEach{
                    categoryList.add(it.getValue(SportsCatergory::class.java)!!)
                }
                selectedCategoryBinding.recyclerViewCategories.adapter = SelectSportsCategoriesAdapter(null,null,
                    this@SelectCategory,categoryList,
                    null,null,
                    selectedCategoryBinding.nextButton)
            }

        })
    }


}
