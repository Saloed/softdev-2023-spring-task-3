package com.example.be.ui.fragments

import android.widget.TextView
import com.example.be.R
import com.example.be.utilits.MESSAGE

class InTextMessageFragment : BaseFragment(R.layout.fragment_in_text_message) {
    private lateinit var nameOfMessage: TextView
    private lateinit var messageText: TextView

    override fun onStart() {
        super.onStart()
        initFields()
    }

    private fun initFields() {
        nameOfMessage = view?.findViewById(R.id.nameOfMessage)!!
        messageText = view?.findViewById(R.id.messageText)!!

        nameOfMessage.text = MESSAGE.title
        messageText.text = MESSAGE.text
    }


}