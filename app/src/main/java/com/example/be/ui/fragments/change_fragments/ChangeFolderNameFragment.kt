package com.example.be.ui.fragments.change_fragments

import android.widget.Button
import android.widget.EditText
import com.example.be.R
import com.example.be.activity.APP_ACTIVITY
import com.example.be.ui.fragments.BaseFragment
import com.example.be.utilits.CHILD_FOLDERS
import com.example.be.utilits.CURRENT_UID
import com.example.be.utilits.FOLDER
import com.example.be.utilits.NODE_USERS
import com.example.be.utilits.REF_DATABASE_ROOT
import com.example.be.utilits.showToast

class ChangeFolderNameFragment : BaseFragment(R.layout.fragment_change_folder_name) {
    lateinit var folderNameChange: EditText
    lateinit var btnDone: Button

    override fun onStart() {
        super.onStart()
        initFields()
        registerEvent()
    }

    private fun initFields() {
        folderNameChange = view?.findViewById(R.id.enter_edit_folder_name)!!
        btnDone = view?.findViewById(R.id.done_edit_folder_name)!!
    }

    private fun registerEvent() {
        btnDone.setOnClickListener {
            if ((folderNameChange.text).isEmpty()) {
                showToast("Введите название")
            } else {
                FOLDER.name = folderNameChange.text.toString()
                REF_DATABASE_ROOT.child(NODE_USERS)
                    .child(CURRENT_UID)
                    .child(CHILD_FOLDERS)
                    .child(FOLDER.id)
                    .child("name")
                    .setValue(FOLDER.name)
                APP_ACTIVITY.supportFragmentManager.popBackStack()
            }

        }
    }


}