package com.example.be.ui.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.be.AppVoiceRecorder
import com.example.be.R
import com.example.be.RECORD_AUDIO
import com.example.be.activity.APP_ACTIVITY
import com.example.be.checkAppPermission
import com.example.be.utilits.CURRENT_UID
import com.google.firebase.database.FirebaseDatabase

class CreateMessageFragment : BaseFragment(R.layout.fragment_create_message) {
    private lateinit var mAppVoiceRecorder: AppVoiceRecorder
    private lateinit var btnVoice: ImageView
    private lateinit var voiceText: TextView



    override fun onStart() {
        super.onStart()

        initFields()
        initFunc()
        registerEvent()
    }


    private fun initFields() {
        mAppVoiceRecorder = AppVoiceRecorder()
        btnVoice = view?.findViewById(R.id.btn_voice)!!
        voiceText = view?.findViewById(R.id.voiceText)!!

    }

    private fun initFunc() {
        TODO("Not yet implemented")
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun registerEvent() {
        btnVoice.setOnTouchListener { v, event ->
            if (checkAppPermission(RECORD_AUDIO)) {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    /*record*/
                    voiceText.text = "Запись"
                    btnVoice.setColorFilter(ContextCompat.getColor(APP_ACTIVITY, R.color.white))
                    Log.d("MyLog", "btnVoice")
                    val messageKey = FirebaseDatabase.getInstance()
                        .getReference("Users/$CURRENT_UID/Folders/nameFolder$")
                        .child("Message").push().key.toString()
                    Log.d("MyLog", "messageKey $messageKey")
                    mAppVoiceRecorder.startRecord(messageKey)
                    Log.d("MyLog", "startRecord")

                } else if (event.action == MotionEvent.ACTION_UP) {
                    /*stop record*/
                    btnVoice.colorFilter = null
                    voiceText.text = "Записано"
                    Log.d("MyLog", "before startRecord")
                    mAppVoiceRecorder.stopRecord { file, messageKey ->
                        //uploadFileToStorage(Uri.fromFile(file), messageKey)/*загружает всё, если хорошо закончилось*/
                    }
                    Log.d("MyLog", "stopRecord")
                }
            }
            true
        }
    }

}