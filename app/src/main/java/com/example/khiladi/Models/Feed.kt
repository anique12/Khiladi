package com.example.khiladi.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Feed(var postList:ArrayList<Post>? = null,
                var desc:String? = null,var catergory: SportsCatergory? =null):Parcelable {
}