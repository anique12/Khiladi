package com.example.khiladi.Resgiter


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController

import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentBasicInfoBinding
import kotlinx.android.synthetic.main.fragment_basic_info.*
import kotlinx.android.synthetic.main.fragment_basic_info.view.*


class BasicInfo : Fragment(),AdapterView.OnItemSelectedListener {

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        playerGender = gender[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val gender = arrayOf("Male","Female")
    lateinit var basicInfoBinding : FragmentBasicInfoBinding
    private var playerGender = String()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
          basicInfoBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_basic_info,container,false)
            basicInfoBinding.ccp.registerPhoneNumberTextView(basicInfoBinding.phone)
            getGender()

         basicInfoBinding.nextButton.setOnClickListener {
             if(validateUsername() && validateCity() && validatePhone()){
                 val bundle = Bundle()
                 bundle.putString("name",username.text.toString())
                 bundle.putString("city",city.text.toString())
                 bundle.putString("gender",playerGender)
                 bundle.putString("phone",ccp.phoneNumber.toString())
                 bundle.putString("country",ccp.selectedCountryName.toString())
                 findNavController().navigate(R.id.action_basicInfo_to_playingSports,bundle)
             }
         }
    return basicInfoBinding.root

    }

    private fun validateUsername(): Boolean {
        if(basicInfoBinding.username.text.isEmpty()){
            basicInfoBinding.usernameLayout.boxStrokeColor = resources.getColor(android.R.color.holo_red_dark)
            return false
        }
        return true
    }

    private fun validateCity(): Boolean {
        if(basicInfoBinding.city.text.isEmpty()){
            basicInfoBinding.cityLayout.boxStrokeColor = resources.getColor(android.R.color.holo_red_dark)
            return false
        }
        return true
    }

    private fun validatePhone(): Boolean {
        if(!basicInfoBinding.ccp.isValid){
            phoneLayout.boxStrokeColor = resources.getColor(android.R.color.holo_red_dark)
            Toast.makeText(context,"Phone invalid",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun getGender() {
        basicInfoBinding.genderSpinner.onItemSelectedListener = this
        val aa = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, gender)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        basicInfoBinding.genderSpinner.adapter = aa
    }


}
