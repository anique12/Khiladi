package com.example.khiladi.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Slot(var time:String?=null,var book:Boolean? = false,var dayTime : String? =null):Parcelable {
}