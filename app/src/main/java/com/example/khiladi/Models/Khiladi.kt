package com.example.khiladi.Models

import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import android.provider.ContactsContract
import com.example.khiladi.Resgiter.IntrestedSports
import com.example.khiladi.Resgiter.PlayingSports
import com.google.firebase.database.FirebaseDatabase

@Parcelize
data class Khiladi(val uid :String = "",val name:String="",
                   var email:String="" ,var profilepic:String="",
                   var city:String="",var country:String="",var gender:String="",
                   var phone:String="",var playingSports:HashMap<String,String>?=null,var intrestedSports: HashMap<String,String>? =null,
                   var fans:HashMap<String,String>? = null,var teams : HashMap<String,HashMap<String,String>>? = null,
                   var ads : HashMap<String,String>? = null):Parcelable
