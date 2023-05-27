package com.example.be.ui.fragments

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.be.R
import com.example.be.ui.fragments.change_fragments.ChangeFullnameFragment
import com.example.be.utilits.USER
import com.example.be.utilits.replaceFragment

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private lateinit var changeFullname: Button
    private lateinit var fullName: TextView
    private lateinit var image: ImageView



    override fun onResume() {
        super.onResume()
        initFields()
        initFunc()

    }



    private fun initFields() {
        changeFullname = view?.findViewById(R.id.change_fullname)!!
        fullName = view?.findViewById(R.id.fullName)!!
        fullName.text = USER.fullname
        image = view?.findViewById(R.id.imageView3)!!
        image.setImageResource(R.drawable.baseline_person_24)

    }

    private fun initFunc() {
        registerEvents()
    }

    private fun registerEvents() {
        changeFullname.setOnClickListener {
            replaceFragment(ChangeFullnameFragment())
        }
    }



}