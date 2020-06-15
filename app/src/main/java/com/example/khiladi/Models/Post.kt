package com.example.khiladi.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(var url: String? = null,var type:String? = null):Parcelable