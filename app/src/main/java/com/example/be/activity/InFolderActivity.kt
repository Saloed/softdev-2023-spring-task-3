package com.example.be.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.be.R
import com.example.be.databinding.ActivityInFolderBinding
import com.example.be.ui.fragments.CreateMessageFragment
import com.example.be.ui.objects.AppDrawer
import com.example.be.utilits.initFirebase
import com.example.be.utilits.replaceFragment

class InFolderActivity : AppCompatActivity() {

    private lateinit var mToolbar: Toolbar
    lateinit var binding: ActivityInFolderBinding
    lateinit var nameFolder: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFirebase()
        initFields()
        initFunc()

        registerEvents()
    }

    private fun initFields() {
        nameFolder = findViewById(R.id.nameOfFolder)
        val bundle: Bundle? = intent.extras /*получаю значения с другого активити*/
        val name: String? = bundle?.getString("folderName")
        val idFolder: String? = bundle?.getString("folderId")
        nameFolder.text = name
        initToolbar()
    }

    private fun initToolbar() {
        mToolbar = binding.inFolderToolbar
        setSupportActionBar(mToolbar)
        this@InFolderActivity.supportActionBar?.setHomeButtonEnabled(true)
        this@InFolderActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initFunc() {

    }


    private fun registerEvents() {
        binding.addMessage.setOnClickListener {
            replaceFragment(CreateMessageFragment())
            /*startActivity(
                Intent(this, CreateMessage::class.java).putExtra(
                    "nameFolder",
                    nameFolder.text
                )
            )*/
        }
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            android.R.id.home -> {
                this.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }*/

}