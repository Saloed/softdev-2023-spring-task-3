package com.example.be.ui.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.be.ui.fragments.voice.AppVoiceRecorder
import com.example.be.R
import com.example.be.utilits.RECORD_AUDIO
import com.example.be.utilits.APP_ACTIVITY
import com.example.be.utilits.COUNT_SNAPSHOT
import com.example.be.utilits.checkAppPermission
import com.example.be.models.Message
import com.example.be.utilits.CHILD_FOLDERS
import com.example.be.utilits.CURRENT_UID
import com.example.be.utilits.FOLDER
import com.example.be.utilits.FOLDER_VOICE_RECORDER
import com.example.be.utilits.ID_MESSAGE
import com.example.be.utilits.MESSAGE
import com.example.be.utilits.NODE_USERS
import com.example.be.utilits.REF_DATABASE_ROOT
import com.example.be.utilits.REF_STORAGE_ROOT
import com.example.be.utilits.TEXT_MESSAGE
import com.example.be.utilits.TITLE_MESSAGE
import com.example.be.utilits.TYPE_MESSAGE
import com.example.be.utilits.TYPE_TEXT
import com.example.be.utilits.TYPE_VOICE
import com.example.be.utilits.VOICE_URL
import com.example.be.utilits.getUrlFromStorage
import com.example.be.utilits.putFileToStorage
import com.example.be.utilits.showToast
import com.google.firebase.database.DatabaseReference

class CreateMessageFragment : BaseFragment(R.layout.fragment_create_message) {
    private lateinit var mAppVoiceRecorder: AppVoiceRecorder
    private lateinit var btnVoice: ImageView
    private lateinit var voiceText: TextView
    private lateinit var hiText: CardView
    private lateinit var cardViewWrite: CardView
    private lateinit var writeText: EditText
    private lateinit var title: EditText
    private lateinit var recordingText: TextView
    private lateinit var btnDoneMessage: Button
    private lateinit var refToWritingMassage: DatabaseReference


    override fun onStart() {
        super.onStart()
        initFields()
        initFunc()
    }


    private fun initFields() {
        mAppVoiceRecorder = AppVoiceRecorder()
        with(binding) {
            btnVoice = findViewById(R.id.btn_voice)
            /*voiceText = view?.findViewById(R.id.voiceText)!!*/
            hiText = findViewById(R.id.cardViewHiText)
            cardViewWrite = findViewById(R.id.cardViewWriteMessage)
            writeText = findViewById(R.id.writeYourText)
            recordingText = findViewById(R.id.textView4)
            btnDoneMessage = findViewById(R.id.buttonCreateMessageDone)
            title = findViewById(R.id.writeTitleOfMessage)
        }


        refToWritingMassage = REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(CURRENT_UID)
            .child(CHILD_FOLDERS)
            .child(FOLDER.id)


        if (COUNT_SNAPSHOT == 0) {
            binding.setOnClickListener {
                hiText.visibility = View.GONE
                cardViewWrite.visibility = View.VISIBLE
                btnDoneMessage.visibility = View.VISIBLE
            }
        } else {
            hiText.visibility = View.GONE
            cardViewWrite.visibility = View.VISIBLE
            btnDoneMessage.visibility = View.VISIBLE
        }
    }

    private fun initFunc() {
        registerEvent()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun registerEvent() {

        writeText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val string = writeText.text.toString()
                if (string.isNotEmpty()) {
                    recordingText.text = "Записываем"
                    btnVoice.visibility = View.GONE
                } else {
                    recordingText.text = "Напишите"
                    btnVoice.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnDoneMessage.setOnClickListener {
            val messageKey = refToWritingMassage.push().key.toString()
            val map: MutableMap<String, Any> = mutableMapOf()
            map[TEXT_MESSAGE] = writeText.text.toString()
            map[TYPE_MESSAGE] = TYPE_TEXT
            map[ID_MESSAGE] = messageKey
            map[TITLE_MESSAGE] = title.text.toString()
            MESSAGE =
                Message(writeText.text.toString(), TYPE_TEXT, messageKey, title.text.toString())

            refToWritingMassage.child(messageKey).updateChildren(map)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast("Всё отлично!")
                        APP_ACTIVITY.supportFragmentManager.popBackStack()
                    }
                }
        }

        btnVoice.setOnTouchListener { _, event ->
            if (checkAppPermission(RECORD_AUDIO)) {
                writeText.visibility = View.GONE
                if (event.action == MotionEvent.ACTION_DOWN) {

                    /*record*/
                    recordingText.text = "Запись"
                    btnDoneMessage.visibility = View.INVISIBLE
                    btnVoice.setColorFilter(ContextCompat.getColor(APP_ACTIVITY, R.color.white))

                    val messageKey = REF_DATABASE_ROOT
                        .child(NODE_USERS)
                        .child(CURRENT_UID)
                        .child(CHILD_FOLDERS)
                        .child(FOLDER.id).push().key.toString()

                    mAppVoiceRecorder.startRecord(messageKey)

                } else if (event.action == MotionEvent.ACTION_UP) {
                    /*stop record*/
                    btnVoice.colorFilter = null
                    recordingText.text = "Записано"
                    mAppVoiceRecorder.stopRecord { file, messageKey ->
                        uploadFileToStorage(Uri.fromFile(file), messageKey)/*загружает всё, если хорошо закончилось*/
                    }
                    APP_ACTIVITY.supportFragmentManager.popBackStack()
                }
            }
            true
        }
    }

    private fun uploadFileToStorage(uri: Uri, messageKey: String) {
        val path = REF_STORAGE_ROOT.child(FOLDER_VOICE_RECORDER).child(messageKey)
        putFileToStorage(uri, path) {
            getUrlFromStorage(path) {
                val map: MutableMap<String, Any> = mutableMapOf()
                map[TEXT_MESSAGE] = ""
                map[TYPE_MESSAGE] = TYPE_VOICE
                map[ID_MESSAGE] = messageKey
                map[TITLE_MESSAGE] = title.text.toString()
                map[VOICE_URL] = it
                MESSAGE = Message(
                    writeText.text.toString(),
                    TYPE_TEXT,
                    messageKey,
                    title.text.toString(),
                    it
                )

                refToWritingMassage.child(messageKey).updateChildren(map)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            showToast("Всё отлично!")
                        }
                    }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mAppVoiceRecorder.releaseRecorder()
    }


}