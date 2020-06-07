package com.example.khiladi.fragments.accountFragments


import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.khiladi.R
import com.example.khiladi.Utils
import com.example.khiladi.databinding.FragmentLoginBinding
import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class Login : Fragment() {

    private lateinit var fragmentLoginBinding: FragmentLoginBinding
    private lateinit var callbackManager: CallbackManager
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var emailText : String
    private lateinit var passwordText : String
    private lateinit var email : TextView
    private lateinit var password : TextView
    private lateinit var currentUser : FirebaseUser
    private lateinit var utils : Utils
    private var currentUserId = String()
    private var firebaseDatabase =  FirebaseDatabase.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login,container,false)
        utils = Utils(context!!)
        hideKeyboard()

         emailText = String()
         passwordText = String()
         email = fragmentLoginBinding.emailLoginFragment
         password = fragmentLoginBinding.passwordLoginFragment
         firebaseAuth = FirebaseAuth.getInstance()
         firebaseDatabase = FirebaseDatabase.getInstance()


        fragmentLoginBinding.signUpLoginFragment.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
        fragmentLoginBinding.forgetPasswordLoginFragment.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_resetPassword)
        }
        fragmentLoginBinding.loginBtnLoginFragment.setOnClickListener {
            getDataAndSignIn()
        }

        return fragmentLoginBinding.root
    }


    private fun getDataAndSignIn() {

        if (validateEmail() && validatePassword()){
            hideKeyboard()
            falseFocus()
            utils.showLoading()
            firebaseAuth.signInWithEmailAndPassword(emailText,passwordText).addOnSuccessListener {
                currentUser = FirebaseAuth.getInstance().currentUser!!
                currentUserId = currentUser.uid
                when {
                    emailVerified() -> {
                        utils.hideLoading()
                        trueFocus()
                        findNavController().navigate(R.id.action_login_to_home)
                    }
                    else -> {
                        utils.hideLoading()
                        trueFocus()
                        findNavController().navigate(R.id.action_login_to_verifyEmail)
                    }
                }
            }.addOnFailureListener{
                utils.hideLoading()
                trueFocus()
                Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
            }
        }
        else{
            validateEmail()
            validatePassword()
        }
    }

    private fun emailVerified():Boolean{
        return currentUser.isEmailVerified
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



    private fun falseFocus(){
        fragmentLoginBinding.apply {
            emailLoginFragment.isFocusableInTouchMode = false
            passwordLoginFragment.isFocusableInTouchMode = false
            emailLoginFragment.isEnabled = false
            passwordLoginFragment.isEnabled = false
            facebookLoginbtnLoginFragment.isEnabled = false
            signUpLoginFragment.isEnabled = false
            forgetPasswordLoginFragment.isEnabled = false
        }

    }

    private fun trueFocus(){
        fragmentLoginBinding.apply {
            emailLoginFragment.isFocusableInTouchMode = true
            passwordLoginFragment.isFocusableInTouchMode = true
            emailLoginFragment.isEnabled = true
            passwordLoginFragment.isEnabled = true
            facebookLoginbtnLoginFragment.isEnabled = true
            signUpLoginFragment.isEnabled = true
            forgetPasswordLoginFragment.isEnabled = true
        }
    }

    private fun hideKeyboard(){
        val  imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)

    }

}
