package com.example.motivator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.asLiveData
import com.example.motivator.databinding.ActivityDbBinding

class DB : AppCompatActivity() {
    lateinit var binding: ActivityDbBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDbBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NotesDB.getDb(this).userDao().getAllNotes().asLiveData().observe(this){list ->
            binding.textView.text =""
            list.forEach {
                val text = "№${it.id} | Name: ${it.name} | Task: ${it.note} | Until: ${it.date}\n"
                binding.textView.append(text)
            }
        }
    }
    fun goHome(view: View) {
        val i = Intent(this,MainActivity::class.java)
        startActivity(i)
    }
    fun goDelete(view: View){
        startActivity(Intent(this,DeletePage::class.java))
    }
    fun goSavingPage(view: View){
        val i = Intent(this, SavingPage::class.java)
        startActivity(i)
    }
}