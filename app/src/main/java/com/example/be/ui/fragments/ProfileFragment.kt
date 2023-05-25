package com.example.be.ui.fragments

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.be.R
import com.example.be.utilits.USER
import com.example.be.utilits.replaceFragment

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    lateinit var changeFullname: Button
    lateinit var fullName: TextView
    lateinit var image: ImageView

    override fun onResume() {
        super.onResume()
        initFields()
        registerEvents()
    }

    private fun initFields() {
        changeFullname = view?.findViewById(R.id.change_fullname)!!
        fullName = view?.findViewById(R.id.fullName)!!
        fullName.text = USER.fullname
        image = view?.findViewById(R.id.imageView3)!!
        image.setImageResource(R.drawable.avatar)
    }

    private fun registerEvents() {
        changeFullname.setOnClickListener {
            replaceFragment(ChangeFullnameFragment())
        }
    }


}