package com.example.khiladi.fragments


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Models.Khiladi
import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentMoreBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class More : Fragment() {

    lateinit var fragmentMoreBinding : FragmentMoreBinding
    var khiladi : Khiladi? = null
    private var currentUserName = FirebaseAuth.getInstance().currentUser?.displayName
    private var currentUserPhoto = FirebaseAuth.getInstance().currentUser?.photoUrl


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentMoreBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_more,container,false)
        if(arguments!=null){
            firebaseHandling()
        }
        khiladi = Khiladi()
        fragmentMoreBinding.discoverPeople.setOnClickListener {
            findNavController().navigate(R.id.action_more_to_discoverKhiladi)
        }

        fragmentMoreBinding.discoverTeams.setOnClickListener {
            findNavController().navigate(R.id.action_more_to_teams)
        }

        fragmentMoreBinding.logout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_more_to_login)

        }
        fragmentMoreBinding.profileLayout.setOnClickListener {
           findNavController().navigate(R.id.action_more_to_basicInfo)
        }
        firebaseHandling()

        return fragmentMoreBinding.root
    }

    private fun firebaseHandling() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        var khiladi = Khiladi()
        val ref = FirebaseDatabase.getInstance().getReference("khiladi/$uid")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                khiladi = p0.getValue(Khiladi::class.java)!!
                if (khiladi.profilepic != ""){
                    Picasso.get().load(khiladi.profilepic).into(fragmentMoreBinding.profilePicMore)
                }
                fragmentMoreBinding.currentKhiladiname.text = khiladi.name
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context,p0.message,Toast.LENGTH_LONG).show()
            }

        })
/*

        fragmentMoreBinding.currentKhiladiname.text = currentUserName
        Picasso.get().load(currentUserPhoto).stableKey("profileImage").into(fragmentMoreBinding.profilePicMore)
*/


    }


}
