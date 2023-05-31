package com.example.motivator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.motivator.databinding.ActivityDeletePageBinding

class DeletePage : AppCompatActivity() {
    private lateinit var binding: ActivityDeletePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeletePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button2.setOnClickListener {
            Thread {
                NotesDB.getDb(this).userDao()
                    .deleteNote(binding.editTextNumber.text.toString().toInt())
            }.start()
            Toast.makeText(applicationContext,"Напоминание удалено!", Toast.LENGTH_SHORT).show()
            val i = Intent(this, DBPage::class.java)
            startActivity(i)
        }
    }
}