package com.example.khiladi.fragments.accountFragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil

import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

private lateinit var resetPasswordBinding: FragmentResetPasswordBinding
class ResetPassword : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       resetPasswordBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_reset_password,container,false)
        resetPasswordBinding.nextBtnForgetPasswordFragment.setOnClickListener{
            FirebaseAuth.getInstance().sendPasswordResetEmail(resetPasswordBinding.emailResetPasswordFragment.toString())
                .addOnSuccessListener {
                    Toast.makeText(context,"Check your email for reset password",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                }
        }
       return resetPasswordBinding.root
    }


}
