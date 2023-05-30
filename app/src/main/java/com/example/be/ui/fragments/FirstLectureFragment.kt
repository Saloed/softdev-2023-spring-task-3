package com.example.be.ui.fragments

import android.os.AsyncTask
import com.example.be.R
import com.example.be.utilits.showToast
import com.github.barteksc.pdfviewer.PDFView
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class FirstLectureFragment(path: String) : BaseFragment(R.layout.fragment_first_lecture) {

    lateinit var pdfView:PDFView
    val url: String = path
    override fun onStart() {
        super.onStart()
        pdfView = view?.findViewById(R.id.pdfview)!!
        RetrievePDFfromUrl(pdfView).execute(url)
    }

    class RetrievePDFfromUrl(pdfView: PDFView) : AsyncTask<String, Void, InputStream>() {
        val pdf = pdfView
        override fun doInBackground(vararg params: String?): InputStream? {
            var inputStream: InputStream? = null
            try {
                val url = URL(params[0])
                val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                inputStream = BufferedInputStream(urlConnection.inputStream)
            } catch (e: Exception) {
                showToast(e.message.toString())
            }
            return inputStream
        }

        override fun onPostExecute(result: InputStream?) {
            super.onPostExecute(result)
            pdf.fromStream(result).load()
        }
    }
}