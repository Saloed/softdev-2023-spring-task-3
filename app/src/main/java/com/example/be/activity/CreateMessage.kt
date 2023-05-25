package com.example.be.activity

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.example.be.AppVoiceRecorder
import com.example.be.utilits.CURRENT_UID
import com.example.be.R
import com.example.be.RECORD_AUDIO
import com.example.be.checkAppPermission
import com.example.be.databinding.ActivityCreateMessageBinding
import com.example.be.utilits.showToast
import com.google.firebase.database.FirebaseDatabase

class CreateMessage : AppCompatActivity() {
    lateinit var binding: ActivityCreateMessageBinding
    var nameFolder: String? = null

    lateinit var mAppVoiceRecorder: AppVoiceRecorder

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras /*получаю значения с другого активити*/
        nameFolder = bundle?.getString("nameFolder")

        mAppVoiceRecorder = AppVoiceRecorder()

        supportActionBar?.title = "BE"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        registerEvent()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun registerEvent () {
        binding.btnVoice.setOnTouchListener { v, event ->
            if (checkAppPermission(RECORD_AUDIO)) {
                if (event.action == MotionEvent.ACTION_DOWN) {
                        /*record*/
                    binding.voiceText.text = "Запись"
                    binding.btnVoice.setColorFilter(ContextCompat.getColor(this, R.color.white))
                    Log.d("MyLog", "btnVoice")
                    val messageKey = FirebaseDatabase.getInstance()
                        .getReference("Users/$CURRENT_UID/Folders/$nameFolder")
                        .child("Message").push().key.toString()
                    Log.d("MyLog", "messageKey $messageKey")
                    mAppVoiceRecorder.startRecord(messageKey)
                    Log.d("MyLog", "startRecord")

                } else if (event.action == MotionEvent.ACTION_UP) {
                    /*stop record*/
                    binding.btnVoice.colorFilter = null
                    binding.voiceText.text = "Записано"
                    Log.d("MyLog", "before startRecord")
                    mAppVoiceRecorder.stopRecord { file, messageKey ->
                        uploadFileToStorage(Uri.fromFile(file), messageKey)/*загружает всё, если хорошо закончилось*/
                    }
                    Log.d("MyLog", "stopRecord")
                }
            }
            true
        }
    }

    private fun uploadFileToStorage(fromFile: Uri, messageKey: String) {
        showToast("Всё отлично!")
    }




    override fun onDestroy() {
        super.onDestroy()
        mAppVoiceRecorder.releaseRecorder()
    }

}

