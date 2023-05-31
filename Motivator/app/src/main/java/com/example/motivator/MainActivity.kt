package com.example.motivator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun goSavingPage(view: View){
        val i = Intent(this, SavingPage::class.java)
        startActivity(i)
    }
    fun goDB(view: View) {
        val i = Intent(this,DBPage::class.java)
        startActivity(i)
    }
}