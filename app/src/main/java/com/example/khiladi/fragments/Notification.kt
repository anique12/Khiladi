package com.example.khiladi.fragments


import com.example.khiladi.Adapters.NotificationAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.khiladi.Models.Notification
import com.example.khiladi.NotificationHandling
import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentNotificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Notification : Fragment(), NotificationAdapter.BackToNotification {

    override fun moveToPlayerProfile(notification: Notification) {
        val bundle = Bundle()
        bundle.putString("khiladiId",notification.fromId)
        notificationclass.readTrue("notification/${currentUser?.uid}/${notification.id}/read")
        findNavController().navigate(R.id.action_notification_to_khiladiProfile,bundle)

    }

    override fun delete(notification: Notification) {
        notificationclass.deleteNotification("notification/${currentUser?.uid}/${notification.id}")
    }

    override fun moveToTeamProfile(notification: Notification) {
        val bundle = Bundle()
        bundle.putParcelable("notification",notification)
        notificationclass.readTrue("notification/${currentUser?.uid}/${notification.id}/read")
        findNavController().navigate(R.id.action_notification_to_teamProfile,bundle)
    }



    var list = ArrayList<Notification>()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private var currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var notificationclass : NotificationHandling

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentNotificationBinding>(inflater, R.layout.fragment_notification,container,false)
        notificationclass = NotificationHandling(context!!)
        firebaseDatabase.getReference("notification/${currentUser?.uid}").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
               p0.children.forEach{
                   val notification = it.getValue(Notification::class.java)
                   list.add(notification!!)
               }
                list.reverse()
                binding.recyclerViewNotification.adapter = NotificationAdapter(list,this@Notification)
            }
        })

        return binding.root
    }


}
