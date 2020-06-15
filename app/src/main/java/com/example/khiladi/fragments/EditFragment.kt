package com.example.khiladi.fragments

import android.app.Dialog
import android.content.Context
import android.icu.text.CaseMap
import android.os.Bundle
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
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.khiladi.PlaceDescription
import com.example.khiladi.R
import com.example.khiladi.databinding.FragmentEditBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditFragment : BottomSheetDialogFragment() {

    interface Editor{
        fun title(title : String)
        fun description(description: String)
    }
    private lateinit var editFragmentBinding : FragmentEditBinding
    private var title : String ? = null
    private var description : String ? = null
    private var newtitle : String ? = null
    private lateinit var editpLace: EditPlace
    private lateinit var listener : Editor
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editFragmentBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_edit, container, false)
        if(arguments?.getString("title") != null){
            title = arguments?.getString("title")!!
            editFragmentBinding.editText.setText(title)
        }
        else if(arguments?.getString("description") != null){
            description = arguments?.getString("description")!!
            editFragmentBinding.editText.setText(title)
        }
        listener = targetFragment as EditPlace


        editFragmentBinding.save.setOnClickListener {
            if(title != null){
                newtitle = editFragmentBinding.editText.text.toString()
                listener.title(newtitle!!)
                this.dismiss()
            }
            else if(title != null){
                listener.description(editFragmentBinding.editText.text.toString())
                this.dismiss()
            }
        }

        return editFragmentBinding.root
    }

}