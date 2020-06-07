package com.example.khiladi.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.net.URL

@Parcelize
data class Notification(var id :String? ="",var imageUrl: String="", var time:Long?= null,
                        var description:String="",var toId:String?="",
                        var fromId:String="",var type:String?="",var category : String? ="",
                        var teamId : String="",var accept :Boolean? = null,var read:Boolean? = false,val eventRef:String?=null
):Parcelable