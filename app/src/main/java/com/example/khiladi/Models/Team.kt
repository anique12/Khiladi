package com.example.khiladi.Models

import android.os.Parcelable
import android.provider.ContactsContract
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp

@Parcelize
data class Team(var id : String? = "" ,var profile:String = "", var name:String="", var category:String="", var status : String? ="",var description:String? = "",
                var players:HashMap<String,String>? = null,var timestamp: String? ="",var captainId:String? =null):Parcelable
