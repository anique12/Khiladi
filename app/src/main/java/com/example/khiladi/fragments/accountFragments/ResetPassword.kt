package com.example.khiladi.fragments.accountFragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentResetPasswordBinding

private lateinit var resetPasswordBinding: FragmentResetPasswordBinding
class ResetPassword : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       resetPasswordBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_reset_password,container,false)
       return resetPasswordBinding.root
    }


}
