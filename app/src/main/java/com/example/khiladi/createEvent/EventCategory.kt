package com.example.khiladi.createEvent


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.khiladi.R
import com.example.khiladi.fragments.Home
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_event_category.view.*

/**
 * A simple [Fragment] subclass.
 */
class EventCategory : BottomSheetDialogFragment() {

    lateinit var eventCategoryView: View
    lateinit var listener : BackToHome
    interface BackToHome{
        fun back(eventType : String)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setStyle(STYLE_NO_FRAME,R.style.BottomSheetDialogTheme)
        dialog?.window?.setWindowAnimations(R.style.DialogAnimation)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        eventCategoryView =  inflater.inflate(R.layout.fragment_event_category, container, false)
        listener = this.targetFragment as Home
        eventCategoryView.singleEvent.setOnClickListener {
            dialog?.dismiss()
            listener.back("single")
        }
        eventCategoryView.teamEvent.setOnClickListener {
            dialog?.dismiss()
            listener.back("team")
        }
        return eventCategoryView
    }



}
