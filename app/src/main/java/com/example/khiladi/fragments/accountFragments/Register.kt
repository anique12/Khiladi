package com.example.khiladi.fragments.accountFragments


import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Models.Khiladi

import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase


class Register : Fragment() {

    private lateinit var registerBinding: FragmentRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var emailText : String
    private lateinit var passwordText : String
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var confirmPassword : EditText
    private lateinit var confirmPasswordText : String
    private lateinit var userNameText : String
    private lateinit var userName : EditText
    private var firebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        registerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        hideKeyboard()
        emailText = String()
        passwordText = String()
        confirmPasswordText = String()
        email = registerBinding.emailRegisterFragment
        password = registerBinding.passwordRegisterFragment
        confirmPassword = registerBinding.confirmPasswordRegisterFragment
        userName = registerBinding.userNameRegisterFragment
        userNameText = String()
        firebaseAuth = FirebaseAuth.getInstance()

        registerBinding.signInLoginFragment.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }

        registerBinding.nextBtnRegisterFragment.setOnClickListener {
            if (validateEmail() && validatePassword() && validatePasswordMatch() && validateUsername()) {
                hideKeyboard()
                falseFocus()
                startLoading()
                createUser()
            }
        }

        return registerBinding.root
    }

    private fun createUser() {
        firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnSuccessListener {
            val userProfileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(userNameText).build()
            firebaseAuth.currentUser?.updateProfile(userProfileChangeRequest)
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            val khiladi = Khiladi(firebaseAuth.currentUser?.uid.toString(),userNameText,emailText)
            firebaseDatabase.getReference("/khiladi/$uid").setValue(khiladi).addOnSuccessListener {
                stopLoading()
                falseFocus()
                Toast.makeText(context,"Your Account has been created successfully",Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_register_to_verifyEmail)
            }

        }.addOnFailureListener{
            stopLoading()
            trueFocus()
            Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
        }
    }


    private fun validateEmail(): Boolean {
        emailText = email.text.toString()
        if (emailText.isEmpty()) {
            email.error = "Please Enter email"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.error = "Please enter valid email"
            return false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        passwordText = password.text.toString()
        if (passwordText.isEmpty()) {
            password.error = "Please enter a password"
            return false
        } else if (passwordText.length < 6) {
            password.error = "Please enter atleast 6 characters"
            return false
        }
        return true
    }

    private fun validateUsername(): Boolean {
        userNameText = userName.text.toString()
        if (userNameText.isEmpty()) {
            userName.error = "Please enter a user name"
            return false
        }
        return true
    }

    private fun validatePasswordMatch():Boolean{
        confirmPasswordText = confirmPassword.text.toString()
        if (confirmPasswordText.isEmpty()) {
            password.error = "Please enter a confirm password"
            return false
        } else if (confirmPasswordText != passwordText){
            password.error = "Password does not match"
            return false
        }
        return true
    }

    private fun stopLoading() {
        registerBinding.nextBtnRegisterFragment.stopAnimation()
        registerBinding.nextBtnRegisterFragment.revertAnimation()
    }

    private fun startLoading() {
        registerBinding.nextBtnRegisterFragment.startAnimation()
    }

    private fun falseFocus() {
        registerBinding.apply {
            emailRegisterFragment.isFocusableInTouchMode = false
            passwordRegisterFragment.isFocusableInTouchMode = false
            emailRegisterFragment.isEnabled = false
            passwordRegisterFragment.isEnabled = false
            signInLoginFragment.isEnabled = false
        }

    }

    private fun trueFocus() {
        registerBinding.apply {
            emailRegisterFragment.isFocusableInTouchMode = true
            passwordRegisterFragment.isFocusableInTouchMode = true
            emailRegisterFragment.isEnabled = true
            passwordRegisterFragment.isEnabled = true
            signInLoginFragment.isEnabled = true
        }
    }

    private fun hideKeyboard(){
        val  imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}