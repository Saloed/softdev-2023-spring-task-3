package com.example.be.ui.fragments.voice

import android.media.MediaPlayer
import com.example.be.utilits.APP_ACTIVITY
import com.example.be.utilits.REF_STORAGE_ROOT
import com.example.be.utilits.showToast
import java.io.File

class AppVoicePlayer {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var file: File

    fun init() {
        mediaPlayer = MediaPlayer()
    }

    fun play(messageKey: String, fileUrl: String, function: () -> Unit) {
        file = File(APP_ACTIVITY.filesDir, messageKey)
        if (file.exists() && file.length() > 0 && file.isFile) {
            startPlay {
                function()
            }
        } else {
            file.createNewFile()
            getFileFromSrorage(file, fileUrl) {
                startPlay {
                    function()
                }
            }
        }
    }

    private fun getFileFromSrorage(file: File, fileUrl: String, function: () -> Unit) {
        val path = REF_STORAGE_ROOT.storage.getReferenceFromUrl(fileUrl)
        path.getFile(file)
            .addOnSuccessListener { function() }
            .addOnFailureListener { showToast(it.message.toString()) }
    }

    private fun startPlay(function: () -> Unit) {
        try {
            mediaPlayer.setDataSource(file.absolutePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { /*полностью проигрался файл*/
                stop {
                    function()
                }
            }
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    fun stop(function: () -> Unit) {
        try {
            mediaPlayer.stop()
            mediaPlayer.reset()
            function()
        } catch (e: Exception) {
            showToast(e.message.toString())
            function()
        }
    }

    fun release() {
        mediaPlayer.release()/*удаляем*/
    }
}