package com.example.be.ui.fragments

import android.animation.ObjectAnimator
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.be.R
import com.example.be.utilits.COUNT_SNAPSHOT_PLUS
import com.example.be.activity.Registration
import com.example.be.ui.fragments.changes.ChangeFullnameFragment
import com.example.be.utilits.AUTH
import com.example.be.utilits.USER
import com.example.be.utilits.replaceActivity
import com.example.be.utilits.replaceFragment
import com.example.be.utilits.showToast

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private lateinit var changeFullname: Button
    private lateinit var fullName: TextView
    private lateinit var image: ImageView
    private lateinit var progressbar: ProgressBar
    private lateinit var range: TextView


    override fun onResume() {
        super.onResume()
        initFields()
        initFunc()
        setHasOptionsMenu(true)
    }


    private fun initFields() {
        with(binding) {
            changeFullname = findViewById(R.id.change_fullname)
            fullName = findViewById(R.id.fullName)
            fullName.text = USER.fullname
            image = findViewById(R.id.imageView3)
            image.setImageResource(R.drawable.baseline_person_24)

            progressbar = findViewById(R.id.progressBar)
            range = findViewById(R.id.range)
        }

        initProgressbar()

    }

    private fun initProgressbar() {
        progressbar.max = 1000
        var currentProgress = 0
        if (COUNT_SNAPSHOT_PLUS > 4) {
            range.text = "4 из 4"
            currentProgress = 1000
            ObjectAnimator.ofInt(progressbar, "progress", currentProgress)
                .setDuration(2000)
                .start()
        } else {
            range.text = "$COUNT_SNAPSHOT_PLUS из 4"
            currentProgress = 250 * COUNT_SNAPSHOT_PLUS
            ObjectAnimator.ofInt(progressbar, "progress", currentProgress)
                .setDuration(2000)
                .start()
        }

    }

    private fun initFunc() {
        registerEvents()
    }

    private fun registerEvents() {
        changeFullname.setOnClickListener {
            replaceFragment(ChangeFullnameFragment())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { /*функция запускается когда нажимаем на какой-нибудь элемент из меню*/
        when (item.itemId) {
            R.id.profile_menu_exit -> {
                AUTH.signOut()
                replaceActivity(Registration())
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_action_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }


}