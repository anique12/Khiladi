package com.example.khiladi.Models

    data class Event(val id : String= "" , val team1 : String? = null
                 , val team2 : String? = null , val timeStamp:String? = ""
                 , val date : String? = "" ,val time : String? = "", val urgent:Boolean? = null,
                 val fixture : Boolean? =null,var category : String? = "",
                 val place : String = "",var responces : HashMap<String,String>? =null,var timeNegotiable : Boolean? = null,
                     var dateNegotiable : Boolean? = null , var placeNegotiable : Boolean? = null, var description : String? = null,val placeRef:String? = null)