package com.example.be.ui.fragments.change_fragments

import android.widget.Button
import android.widget.EditText
import com.example.be.R
import com.example.be.utilits.APP_ACTIVITY
import com.example.be.ui.fragments.BaseFragment
import com.example.be.utilits.CHILD_FULLNAME
import com.example.be.utilits.CURRENT_UID
import com.example.be.utilits.NODE_USERS
import com.example.be.utilits.REF_DATABASE_ROOT
import com.example.be.utilits.USER
import com.example.be.utilits.initFirebase
import com.example.be.utilits.showToast

class ChangeFullnameFragment : BaseFragment(R.layout.fragment_change_fullname) {
    lateinit var fullnameChange: EditText
    lateinit var btnDone: Button

    override fun onResume() {
        super.onResume()
        initFirebase()
        initFields()
        registerEvent()
    }

    private fun initFields() {
        fullnameChange = view?.findViewById(R.id.fullname_change)!!
        btnDone = view?.findViewById(R.id.done)!!
    }

    private fun registerEvent() {
        btnDone.setOnClickListener {
            if ((fullnameChange.text).isEmpty()) {
                showToast("Введите имя")
            } else {
                USER.fullname = fullnameChange.text.toString()
                REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FULLNAME)
                    .setValue(USER.fullname)
                APP_ACTIVITY.supportFragmentManager.popBackStack()
            }

        }

    }
}