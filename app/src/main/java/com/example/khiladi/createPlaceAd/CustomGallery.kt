package com.example.khiladi.createPlaceAd


import ShowPicsAdapter
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.GridLayout
import com.example.khiladi.R
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks
import com.github.ksoichiro.android.observablescrollview.ScrollState
import com.google.android.material.appbar.AppBarLayout
import com.github.ksoichiro.android.observablescrollview.ObservableGridView
import android.widget.RelativeLayout
import android.widget.Toast
import android.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.khiladi.databinding.FragmentCustomGalleryBinding


/**
 * A simple [Fragment] subclass.
 */
class CustomGallery : Fragment(){

    private lateinit var customGalleryBinding: FragmentCustomGalleryBinding
    private var photos : ArrayList<Uri>? = ArrayList()
    private var photosString : ArrayList<String>? = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        customGalleryBinding  = DataBindingUtil.inflate(inflater,R.layout.fragment_custom_gallery, container, false)

        customGalleryBinding.select.setOnClickListener {
            openGallery()
        }
        customGalleryBinding.nextButton.setOnClickListener {
            if(photos!!.isNotEmpty()){
                photos?.forEach {
                    photosString?.add(it.toString())
                }
                val bundle = Bundle()
                bundle.putString("locality",arguments?.getString("locality").toString())
                bundle.putString("country",arguments?.getString("country").toString())
                bundle.putString("latitude",arguments?.getString("latitude").toString())
                bundle.putString("longitude",arguments?.getString("longitude").toString())
                bundle.putString("postalCode",arguments?.getString("postalCode").toString())
                bundle.putString("city",arguments?.getString("city").toString())
                bundle.putString("map",arguments?.getString("map").toString())
                bundle.putStringArrayList("photosString",photosString)
                findNavController().navigate(R.id.action_customGallery_to_description2,bundle)
            }else{
                customGalleryBinding.description.startAnimation(AnimationUtils.loadAnimation(context,R.anim.shake))
            }

        }
        return customGalleryBinding.root
    }

    private fun showPicsInRecyclerView() {
        if(photos != null){
            customGalleryBinding.recyclerViewPics.layoutManager = GridLayoutManager(context,3)
            customGalleryBinding.recyclerViewPics.adapter = ShowPicsAdapter(photos!!)
        }
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101 && resultCode == Activity.RESULT_OK && data != null) {
            val clip = data.clipData
            if(clip?.itemCount == null){
                photos?.add(data.data!!)
            }
            else {
                for (i in 0 until clip.itemCount) {
                    val item = clip.getItemAt(i)
                    val uri = item.uri
                    photos?.add(uri)
                }
            }
        }
        photos?.forEach {
            Log.d("picAdress",it.toString())
        }
        showPicsInRecyclerView()
    }

}
