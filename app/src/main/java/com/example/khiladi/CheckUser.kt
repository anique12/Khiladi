package com.example.khiladi

import com.example.khiladi.Models.Khiladi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

private lateinit var firebaseAuth : FirebaseAuth
private lateinit var firebaseDatabase: FirebaseDatabase
private lateinit var currentUserId  : String
private lateinit var currentUser : FirebaseUser
private lateinit var databaseReference :DatabaseReference


class CheckUser (){

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!
        currentUserId = currentUser.uid
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("/khiladi/$currentUserId")
    }

     public fun userIsNull():Boolean{
         return currentUser == null
    }
     public fun EmailVerified():Boolean{
         return currentUser!!.isEmailVerified
    }

    public fun isVaild():Boolean{
        var check = false
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                val khiladi = p0.getValue(Khiladi::class.java)
                if(khiladi?.email == currentUser?.email){
                    check = true
                }

            }

        })
        return check
    }
}