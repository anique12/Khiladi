package com.example.khiladi.Models


data class Chat(var id:String? = null,var msg:String? = null,var timeStamp:String? = null,
                var toId:String? = null , var fromId : String? = null,var group : Boolean? = false,var category:String? =null)
