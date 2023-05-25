package com.example.be.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.be.R
import com.example.be.databinding.ActivityInFolderBinding

class Profile : AppCompatActivity() {
    lateinit var binding: ActivityInFolderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "BE"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}