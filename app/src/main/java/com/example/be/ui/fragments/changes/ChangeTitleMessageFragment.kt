package com.example.be.ui.fragments.changes

import android.widget.Button
import android.widget.EditText
import com.example.be.R
import com.example.be.utilits.APP_ACTIVITY
import com.example.be.ui.fragments.BaseFragment
import com.example.be.utilits.CHILD_FOLDERS
import com.example.be.utilits.CURRENT_UID
import com.example.be.utilits.FOLDER
import com.example.be.utilits.MESSAGE
import com.example.be.utilits.NODE_USERS
import com.example.be.utilits.REF_DATABASE_ROOT
import com.example.be.utilits.TITLE_MESSAGE
import com.example.be.utilits.showToast

class ChangeTitleMessageFragment : BaseFragment(R.layout.fragment_change_title_message) {
    lateinit var titleChange: EditText
    lateinit var btnDone: Button

    override fun onStart() {
        super.onStart()
        initFields()
        registerEvent()
    }

    private fun initFields() {
        titleChange = view?.findViewById(R.id.enter_edit_title_message)!!
        titleChange.setText(MESSAGE.title)
        btnDone = view?.findViewById(R.id.done_edit_title_message)!!
    }

    private fun registerEvent() {
        btnDone.setOnClickListener {
            if ((titleChange.text).isEmpty()) {
                showToast("Введите название")
            } else {
                MESSAGE.title = titleChange.text.toString()
                REF_DATABASE_ROOT.child(NODE_USERS)
                    .child(CURRENT_UID)
                    .child(CHILD_FOLDERS)
                    .child(FOLDER.id)
                    .child(MESSAGE.id)
                    .child(TITLE_MESSAGE)
                    .setValue(MESSAGE.title)
                APP_ACTIVITY.supportFragmentManager.popBackStack()
            }

        }
    }

}