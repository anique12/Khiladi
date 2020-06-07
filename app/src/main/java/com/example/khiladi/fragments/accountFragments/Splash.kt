package com.example.khiladi.fragments.accountFragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class Splash : Fragment() {

    private lateinit var fragmentSplashBinding: FragmentSplashBinding
    private var currentUser  = FirebaseAuth.getInstance().currentUser
    private  var currentUserId = String()
    private var auth = FirebaseAuth.getInstance()
    private var firebaseDatabase =  FirebaseDatabase.getInstance()
    private var databaseReference =  firebaseDatabase.reference
    private var check = "false"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth.currentUser?.reload()?.addOnSuccessListener{
            currentUser = auth.currentUser
        }
        if (currentUser == null){
            findNavController().navigate(R.id.action_splash_to_login)
        }
        else{
           if (!currentUser!!.isEmailVerified){
               findNavController().navigate(R.id.action_splash_to_verifyEmail)
           }
            else{
               findNavController().navigate(R.id.action_splash_to_home)
           }
        }
    }


   /* private fun isValid() {

        databaseReference.child("khiladi$currentUserId").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                val khiladi = p0.getValue(Khiladi::class.java)
                if (khiladi!=null){
                    val intent = Intent(context,MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    if(currentUser!!.isEmailVerified){
                        findNavController().navigate(R.id.action_splash_to_detailedAccountInfo)
                    }
                    else{
                        findNavController().navigate(R.id.action_splash_to_verifyEmail)
                    }
                }


            }

        })
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentSplashBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_splash,container,false)
        return fragmentSplashBinding.root
    }

}
