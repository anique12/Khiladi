package com.example.khiladi.Models

import android.os.Parcelable
import com.example.khiladi.fragments.CommentModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Feed(var id : String? = null , var postList:ArrayList<Post>? = null,
                var desc:String? = null,var catergory: String? =null , var userId : String? = null
                ,var ts:String? = null,var likes : HashMap<String,String>? = null):Parcelable {
}