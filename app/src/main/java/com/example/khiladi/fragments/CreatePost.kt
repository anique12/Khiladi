package com.example.khiladi.fragments


import ShowPicsAdapter
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.example.khiladi.Models.Feed
import com.example.khiladi.Models.Post
import com.example.khiladi.Models.SportsCatergory

import com.example.khiladi.R
import com.example.khiladi.Utils
import com.example.khiladi.databinding.FragmentCreatePostBinding
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 */
class CreatePost : Fragment(),BottomSheetDialogChooseCategory.SportsCall {

    override fun callBack(selectedCategory: SportsCatergory) {
        selectedSportsCategory = selectedCategory
        Picasso.get().load(selectedCategory.photo).into(creatPostBinding.sportsImage)
        creatPostBinding.post.isEnabled = true
    }

    private lateinit var creatPostBinding: FragmentCreatePostBinding
    private var photos : ArrayList<Uri>? = ArrayList()
    private var photoString : ArrayList<String>? = ArrayList()
    private lateinit var utils : Utils
    private var byteArrayList = ArrayList<ByteArray>()
    private var byteArray = ByteArray(5)
    private var downloadedUrl = ArrayList<Post>()
    private var storage = FirebaseStorage.getInstance()
    private var selectedSportsCategory :SportsCatergory? = null
    private var postHashMap = HashMap<String,String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        creatPostBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_create_post, container, false)
        Picasso.get().load(FirebaseAuth.getInstance().currentUser?.photoUrl).into(creatPostBinding.profileImageView)
        utils = Utils(context!!)
        creatPostBinding.username.text = FirebaseAuth.getInstance().currentUser?.displayName
        creatPostBinding.photoVideo.setOnClickListener {
            openGallery()
        }
        creatPostBinding.desc.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                creatPostBinding.post.isEnabled = p0!!.isNotEmpty()
            }

        })
        creatPostBinding.category.setOnClickListener {
            val fragment = BottomSheetDialogChooseCategory(this)
            fragment.setTargetFragment(this,1)
            fragment.show(fragmentManager!!, "")
        }
        creatPostBinding.post.setOnClickListener {
            if(selectedSportsCategory == null) {
                Toast.makeText(context,"Please select sports category",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                Toast.makeText(context,"Posting...",Toast.LENGTH_SHORT).show()
                if(postHashMap.isNotEmpty()){
                   /* photos?.forEach {
                        photoString?.add(it.toString())
                    }*/
                    storePicsToStorage()
                }
                else{
                    storeToFirebase(creatPostBinding.desc.text.toString())
                }
            }

        }
        return creatPostBinding.root
    }


        private fun storePicsToStorage() {
            findNavController().navigate(R.id.action_createPost_to_feed)
            Log.d("photosString",photoString.toString())
            /*photoString?.forEach {
                val uri = Uri.parse(it)
                val bmp = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
                val baos = ByteArrayOutputStream()
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos)
                byteArray.add(baos.toByteArray())
            }*/
            postHashMap.forEach{map->

                val filename = UUID.randomUUID().toString()
                val ref = storage.getReference("post/$filename")

                if(map.key == "image"){
                    val uri = Uri.fromFile(File(map.value))
                    Log.d("uri",uri.toString())
                    Toast.makeText(context, uri.toString(), Toast.LENGTH_SHORT).show()
                    val bmp = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
                    val baos = ByteArrayOutputStream()
                    bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos)
                    ref.putBytes(baos.toByteArray()).addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener {
                            downloadedUrl.add(Post(it.toString(),map.key))
                            Log.d("downloadedUrl",it.toString())
                            if(postHashMap.size == downloadedUrl.size){
                                storeToFirebase(creatPostBinding.desc.text.toString())
                            }
                        }
                    }.addOnFailureListener{
                        utils.hideLoading()
                        Toast.makeText(context,it.message, Toast.LENGTH_SHORT).show()
                    }
                }

                val file = File(map.value)
                val downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val desFile = File(downloadsPath, "${System.currentTimeMillis()}_${file.name}")
                if(map.key == "video") {
                    VideoCompressor.start(
                        map.value,
                        desFile.path,
                        object : CompressionListener {
                            override fun onProgress(percent: Float) {
                                // Update UI with progress value
                            }

                            override fun onStart() {
                                // Compression start
                            }

                            override fun onSuccess() {
                                val uri = Uri.fromFile(File(desFile.path))
                                Log.d("uri", uri.toString())
                                ref.putFile(uri).addOnSuccessListener {
                                    ref.downloadUrl.addOnSuccessListener {
                                        desFile.delete()
                                        downloadedUrl.add(Post(it.toString(),map.key))
                                        Log.d("downloadedUrl", it.toString())
                                        if(postHashMap.size == downloadedUrl.size){
                                            storeToFirebase(creatPostBinding.desc.text.toString())
                                        }
                                    }
                                }.addOnFailureListener {
                                    utils.hideLoading()
                                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                                }

                            }

                            override fun onFailure() {
                                // On Failure
                            }

                            override fun onCancelled() {
                                // On Cancelled
                            }

                        }, VideoQuality.MEDIUM, isMinBitRateEnabled = false
                    )

                }
                    //byteArray = baos.toByteArray()

            }
        //    storeToFirebase(desc = creatPostBinding.desc.text.toString())
            Log.d("byteArray",byteArray.toString())
            /*byteArray.forEach {
                val filename = UUID.randomUUID().toString()
                val ref = storage.getReference("post/$filename")
                ref.putBytes(it).addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        downloadedUrl.add(it.toString())
                        Log.d("downloadedUrl",it.toString())
                        if(byteArray.size == downloadedUrl.size){
                            //storeToFirebase(creatPostBinding.desc.text.toString())
                        }
                    }
                }.addOnFailureListener{
                    utils.hideLoading()
                    Toast.makeText(context,it.message, Toast.LENGTH_SHORT).show()
                }
            }*/

        }

        private fun storeToFirebase(desc: String) {
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
            val ref  = FirebaseDatabase.getInstance().getReference("feeds/$currentUserId")
            val key = ref.push().key
            val feed = Feed(key,downloadedUrl,desc,selectedSportsCategory?.id,currentUserId,
                System.currentTimeMillis().toString())
            FirebaseDatabase.getInstance().getReference("feeds/$currentUserId/$key").setValue(feed).addOnSuccessListener {
                Toast.makeText(context,"Posted Successfully",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
            }
        }

        private fun openGallery(){
           val  options = Options.init()
          .setRequestCode(101)                                           //Request code for activity results
          .setCount(10)                                                   //Number of images to restict selection count
          .setFrontfacing(false)                                         //Front Facing camera on start
          .setExcludeVideos(false)                                       //Option to exclude videos
          .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
          .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
          .setPath("/pix/images");                                       //Custom Path For media Storage
             Pix.start(this, options);
        }



    private fun showPicsInRecyclerView() {
        if(postHashMap.isNotEmpty()){
            creatPostBinding.recyclerView.layoutManager = GridLayoutManager(context,3)
            val list = ArrayList<Uri>()
            postHashMap.forEach{
                val uri = Uri.parse(it.value)
                list.add(uri)
            }
            creatPostBinding.recyclerView.adapter = ShowPicsAdapter(photos!!)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101 && resultCode == Activity.RESULT_OK && data != null) {
             photoString = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            photoString?.forEach {
                if(it.contains("mp4").or(it.contains("video"))){
                    postHashMap["video"] = it

                }
                else if(it.contains("image").or(it.contains("jpg"))){
                    postHashMap["image"] = it
                }
            }
        }

        photos?.forEach {
          Log.d("picAdress",it.toString())
        }

        creatPostBinding.post.isEnabled = true
           // showPicsInRecyclerView()
    }

}
