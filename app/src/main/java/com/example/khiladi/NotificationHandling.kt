package com.example.khiladi

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.khiladi.Models.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class NotificationHandling(var context: Context) {

    private var currentUser = FirebaseAuth.getInstance().currentUser
    private var firebaseDatabase = FirebaseDatabase.getInstance()

     fun sendFanNotification(toId :String,fromId:String){
        val description = " become your fan"
         val tsLong = System.currentTimeMillis()
         val ref = firebaseDatabase.getReference("notification/$toId").push()
        val notification = Notification(ref.key,currentUser?.photoUrl.toString(),
                           tsLong,description,toId,currentUser!!.uid,"fan",
                          null,fromId,null)
        ref.setValue(notification).addOnFailureListener {
            Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteNotification(path : String){
        firebaseDatabase.getReference(path).removeValue()
    }

    fun readTrue(path: String) {
        firebaseDatabase.getReference(path).setValue(true)
    }

/*
        fun sendRequestToPlayerForTeamPlayer(toId: String) {
        val currentUserPhoto = currentUser?.photoUrl.toString()
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        val description = " send you a request to join "

            val firebaseDatabaseReference = firebaseDatabase.getReference("notification/$toId").push()
            val key = firebaseDatabaseReference.key
            val notification = Notification(key,currentUserPhoto, ts, description, toId, currentUser?.uid!!,"request_join_team",firebaseDatabaseReference.toString()!!,false)
            firebaseDatabaseReference.setValue(notification).addOnSuccessListener {
                //  Toast.makeText(context, "Requests send to your players", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }

    }
*/
}