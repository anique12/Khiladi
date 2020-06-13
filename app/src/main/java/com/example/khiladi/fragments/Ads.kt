package com.example.khiladi.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Models.Ads2
import com.example.khiladi.R
import com.example.khiladi.Utils
import com.example.khiladi.databinding.FragmentAdsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.xwray.groupie.GroupAdapter

class Ads : Fragment() {

    lateinit var adsbinding : FragmentAdsBinding
    private var adsList = ArrayList<Ads2>()
    private lateinit var utils : Utils
    private var currentUser = FirebaseAuth.getInstance().currentUser?.uid
    private var firebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        adsbinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ads, container, false)
        val fragmentAdapter = MyPagerAdapter(activity!!.supportFragmentManager)
        adsbinding.pagerAds.adapter = fragmentAdapter
        utils = Utils(context!!)
        adsbinding.places.setOnClickListener {
            findNavController().navigate(R.id.action_ads_to_places)
        }
        adsbinding.myAds.setOnClickListener {
            findNavController().navigate(R.id.action_ads_to_myAds)
        }
        adsbinding.tabLayoutAds.setupWithViewPager(adsbinding.pagerAds)
        adsbinding.newPlaceAdd.setOnClickListener {
            dexterPermission()
        }
        return adsbinding.root
    }

    private fun dexterPermission(){
        Dexter.withActivity(activity).withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    if(utils.isGPSEnabled()){
                        if(utils.isNetworkConnected()){
                            findNavController().navigate(R.id.action_ads_to_placeLocation)
                        }
                        else{
                            utils.dialogToTurnOnWifi()
                        }
                    }
                    else{
                        utils.showGPSDisabledAlertToUser()
                    }
                }
                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    dexterPermission()
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(context,"You must enable it", Toast.LENGTH_LONG).show()
                    dexterPermission()
                }
            }).check()
    }

}

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> Rent()
            1 -> Rent()
            else -> {
                return Purchase()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Places"
            1 -> "Rent"
            else -> {
                return "Purchase"
            }
        }
    }
}