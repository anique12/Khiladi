package com.example.khiladi.Models

import android.icu.util.Calendar
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ads2(val id : String? = null ,val khiladiId: String? = null,val title : String? = null,
                val description : String? = null, val price: String?= null,
                val priceType : String? = null ,val locality : String? = null,
                val latitude : String? = null,var longitude : String? = null,
                val images: HashMap<String,String>? =null,var mapSnapshot: String? = null,
                val sportsId: String? = null,var ref : String? = null,var timings : ArrayList<String>? = null,
                var otherTimeResponce:Boolean? =null):Parcelable