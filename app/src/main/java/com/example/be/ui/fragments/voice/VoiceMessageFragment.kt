package com.example.be.ui.fragments.voice

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.be.R
import com.example.be.ui.fragments.BaseFragment
import com.example.be.utilits.MESSAGE
import com.example.be.utilits.showToast

class VoiceMessageFragment : BaseFragment(R.layout.new_voice) {
    private lateinit var title: TextView
    private lateinit var btnStart: ImageView
    private lateinit var btnStop: ImageView
    private val appVoicePlayer = AppVoicePlayer()

    override fun onStart() {
        super.onStart()
        initFields()
        registerEvents()
    }

    private fun initFields() {
        title = view?.findViewById(R.id.titleMessageVoice)!!
        title.text = MESSAGE.title
        btnStart = view?.findViewById(R.id.voiceImagePlay)!!
        btnStop = view?.findViewById(R.id.voiceImageStop)!!

    }

    private fun registerEvents() {
        appVoicePlayer.init()
        btnStart.setOnClickListener {
            btnStart.visibility = View.GONE
            btnStop.visibility = View.VISIBLE
            showToast("Начало")
            btnStop.setOnClickListener {
                stop {
                    btnStart.visibility = View.VISIBLE
                    btnStop.visibility = View.GONE
                }
            }
            appVoicePlayer.play(MESSAGE.id, MESSAGE.voiceUrl){
                btnStart.visibility = View.VISIBLE
                btnStop.visibility = View.GONE
                showToast("Конец")
            }

        }
    }

    private fun stop(function: () -> Unit) {
        appVoicePlayer.stop {
            function()
        }
    }

}