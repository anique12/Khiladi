package com.example.khiladi.createPlaceAd


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Models.SportsCatergory
import com.example.khiladi.R
import com.example.khiladi.Utils
import com.example.khiladi.databinding.FragmentDescriptionBinding
import com.google.firebase.database.FirebaseDatabase


class Description : Fragment(),AdapterView.OnItemSelectedListener{

    private lateinit var descriptionBinding: FragmentDescriptionBinding
    private lateinit var utils : Utils
    private var priceType = arrayOf("per Match","per Hour","per Day")
    private var selectedPriceType = String()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        descriptionBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_description, container, false)
        utils = Utils(context!!)
        getPriceType()
        descriptionBinding.nextButton.setOnClickListener {
            if(validateData()){
                navigateToCategory()
            }
        }


        return descriptionBinding.root
    }


    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        selectedPriceType = priceType[p2]
    }


    private fun navigateToCategory() {
        val bundle = Bundle()
        bundle.putString("locality",arguments?.getString("locality").toString())
        bundle.putString("country",arguments?.getString("country").toString())
        bundle.putStringArrayList("photosString",arguments?.getStringArrayList("photosString"))
        bundle.putString("description",descriptionBinding.description.text.toString())
        bundle.putString("title",descriptionBinding.title.text.toString())
        bundle.putString("price",descriptionBinding.price.text.toString())
        bundle.putString("priceType",selectedPriceType)
        bundle.putString("city",arguments?.getString("city").toString())
        bundle.putString("latitude",arguments?.getString("latitude").toString())
        bundle.putString("longitude",arguments?.getString("longitude").toString())
        bundle.putString("map",arguments?.getString("map").toString())
        bundle.putString("postalCode",arguments?.getString("postalCode").toString())
        findNavController().navigate(R.id.action_description2_to_selectCategory2,bundle)
    }

    private fun validateData(): Boolean {
        var validate = true
        if(descriptionBinding.title.text.isEmpty()){
            descriptionBinding.title.startAnimation(AnimationUtils.loadAnimation(context,R.anim.shake))
            validate = false
        }
        if(descriptionBinding.description.text.isEmpty()){
            descriptionBinding.descLayout.startAnimation(AnimationUtils.loadAnimation(context,R.anim.shake))
            validate = false
        }
        if(descriptionBinding.price.text.isEmpty()){
            descriptionBinding.price.startAnimation(AnimationUtils.loadAnimation(context,R.anim.shake))
            validate = false
        }

        if(validate) return true
        return false
    }

    private fun getPriceType() {
        descriptionBinding.priceSpinner.onItemSelectedListener = this
        val aa = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, priceType)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        descriptionBinding.priceSpinner.adapter = aa
    }



}
