package com.example.be.utilits

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.be.R
import com.example.be.activity.APP_ACTIVITY
import com.example.be.activity.COUNT_SNAPSHOT
import com.example.be.activity.COUNT_SNAPSHOT_PLUS
import com.example.be.activity.PREV_COUNT_SNAPSHOT_PLUS
import com.example.be.models.Folder
import com.example.be.models.Message
import com.example.be.ui.fragments.adapters.FolderAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(APP_ACTIVITY, activity::class.java)
    APP_ACTIVITY.startActivity(intent)
    APP_ACTIVITY.finish()
}

fun replaceFragment(fragment: Fragment, addStack:Boolean = true) {
    if (addStack){
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.dataContainer,
                fragment
            ).commit()
    } else {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .replace(R.id.dataContainer,
                fragment
            ).commit()
    }
}

fun countSnapshot(value: Folder?) {
    val databaseReference =
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FOLDERS).child(value!!.id)
    databaseReference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                COUNT_SNAPSHOT_PLUS += snapshot.childrenCount.toInt() - 2
            }
        }
        override fun onCancelled(error: DatabaseError) {
        }
    })
}