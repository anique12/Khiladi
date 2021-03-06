package com.example.khiladi.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_toolbar_profile.view.*


class KhiladiProfile : Fragment() {

    var firebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var thisView : View
    private var khiladi = Khiladi()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        thisView = inflater.inflate(R.layout.fragment_khiladi_profile,container,false)
        if(arguments?.getParcelable<Khiladi>("khiladi") != null){
            khiladi = arguments?.getParcelable<Khiladi>("khiladi")!!
            updateUI(khiladi)
        }
        if (arguments?.getString("khiladiId") != null){
            val khiladiId = arguments?.getString("khiladiId")
            getKhiladi(khiladiId!!)
        }

        thisView.message.setOnClickListener {
            val bundle = Bundle()
            Log.d("chgecking Testing",khiladi.toString())
            Toast.makeText(context,khiladi.name, Toast.LENGTH_SHORT).show()
            bundle.putParcelable("khiladi",khiladi)
            findNavController().navigate(R.id.action_khiladiProfile_to_chatLog,bundle)
        }

        /*if(khiladi.fans?.size == null) fragmentKhiladiProfileBinding.fansAccountFragment.text = "0 fans"
        else fragmentKhiladiProfileBinding.fansAccountFragment.text = khiladi.fans?.size.toString()+" fans"*/
        return thisView
    }

    private fun updateUI(khiladi : Khiladi) {
        if(khiladi.profilepic != "") Picasso.get().load(khiladi.profilepic).into(thisView.img_profile)
        thisView.name?.text = khiladi.name
    }

    private fun getKhiladi(khiladiId: String) {
        firebaseDatabase.getReference("khiladi/$khiladiId").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                khiladi = p0.getValue(Khiladi::class.java)!!
                updateUI(khiladi)
            }

        })
    }


}
