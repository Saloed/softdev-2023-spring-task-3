package com.example.motivator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.motivator.databinding.ActivitySavingPageBinding

class SavingPage : AppCompatActivity() {
    private lateinit var binding: ActivitySavingPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = NotesDB.getDb(this)
        binding.button.setOnClickListener {
            val note = Note(
                null,
                binding.editTextText.text.toString(),
                binding.editTextText2.text.toString(),
                binding.editTextDate.text.toString()
            )
            Thread {
                db.userDao().addNote(note)
            }.start()
            Toast.makeText(applicationContext,"Напоминание создано!", Toast.LENGTH_SHORT).show()
            val i = Intent(this, DB::class.java)
            startActivity(i)
        }
    }
}