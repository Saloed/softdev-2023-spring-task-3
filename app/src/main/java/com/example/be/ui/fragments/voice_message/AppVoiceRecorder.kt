package com.example.be.ui.fragments.voice_message

import android.media.MediaRecorder
import android.util.Log
import com.example.be.utilits.APP_ACTIVITY
import com.example.be.utilits.showToast
import java.io.File

class AppVoiceRecorder {
        private val mMediaRecorder = MediaRecorder()
        private lateinit var mFile: File
        private lateinit var mMessageKey: String

        fun startRecord(messageKey: String) {
            try {
                mMessageKey = messageKey
                createFileForRecord()
                prepareMediaRecorder()
                mMediaRecorder.start()
                Log.d("MyLog", "start")
                showToast("start")
            } catch (e: Exception) {
                showToast(e.message.toString())
            }
        }

        private fun prepareMediaRecorder() {
            mMediaRecorder.reset()
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT)/*откуда получаем данные*/
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            mMediaRecorder.setOutputFile(mFile.absolutePath)/*передали путь*/
            mMediaRecorder.prepare()
        }

        private fun createFileForRecord() {
            mFile = File(APP_ACTIVITY.filesDir, mMessageKey)/*обьявили*/
            mFile.createNewFile()/*создали*/
            Log.d("MyLog", "Вроде как должен создаться файл")
            Log.d("MyLog", mFile.absolutePath)


        }

        fun stopRecord(onSuccess: (file: File, messageKey: String) -> Unit) {
            try {
                mMediaRecorder.stop()
                onSuccess(mFile, mMessageKey)
                Log.d("MyLog", "onSuccess stop")

            } catch (e: Exception) {
                showToast(e.message.toString())
                mFile.delete()
            }
        }

        fun releaseRecorder() { /*удаляем экземпляр из памяти*/
            try {
                mMediaRecorder.release()
            } catch (e: Exception) {
                showToast(e.message.toString())
            }
        }
}