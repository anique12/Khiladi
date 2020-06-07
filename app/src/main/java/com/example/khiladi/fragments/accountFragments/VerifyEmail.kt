package com.example.khiladi.fragments.accountFragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController

import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentVerifyEmailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser



class VerifyEmail : Fragment() {

    private lateinit var verifyEmailBinding: FragmentVerifyEmailBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var emailText : String
    private lateinit var email : TextView
    private lateinit var currentUser: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        verifyEmailBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_verify_email,container,false)
        falseFocus()
        emailText = String()
        email = verifyEmailBinding.emailVerifyVerifyEmailFragment
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser!!
        email.text = currentUser.email
        verifyEmailBinding.nextBtnVerifyEmailFragment.setOnClickListener {
                currentUser.reload().addOnSuccessListener {
                    val user = firebaseAuth.currentUser
                    if (!user!!.isEmailVerified) {
                        Toast.makeText(context, "Please check your email to verify and then continue", Toast.LENGTH_LONG).show()
                    } else {
                       findNavController().navigate(R.id.action_verifyEmail_to_basicInfo2)
                    }
                }
        }
        verifyEmailBinding.verifyEmailBtnVerifyEmailFragment.setOnClickListener {
            startLoading()
            sendEmail()

        }
        return verifyEmailBinding.root
    }

    private fun sendEmail() {
        currentUser.sendEmailVerification().addOnSuccessListener {
            startLoading()
            viewVisible()
        }
    }

    private fun viewVisible() {
        stopLoading()
        makeVisible()
    }


    private fun stopLoading(){
        verifyEmailBinding.verifyEmailBtnVerifyEmailFragment.stopAnimation()
        verifyEmailBinding.verifyEmailBtnVerifyEmailFragment.revertAnimation()
    }

    private fun startLoading(){
        verifyEmailBinding.verifyEmailBtnVerifyEmailFragment.startAnimation()
    }

    private fun falseFocus(){
        verifyEmailBinding.apply {
            emailVerifyVerifyEmailFragment.isFocusableInTouchMode = false
            emailVerifyVerifyEmailFragment.isEnabled = false
        }

    }

    private fun makeVisible(){
        verifyEmailBinding.apply {
            nextBtnVerifyEmailFragment.visibility = View.VISIBLE
            messageVerifyEmailFragment.visibility = View.VISIBLE
        }
    }

}
