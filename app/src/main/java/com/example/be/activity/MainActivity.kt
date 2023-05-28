package com.example.be.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.be.databinding.ActivityMainBinding
import com.example.be.models.User
import com.example.be.ui.fragments.MainFragment
import com.example.be.ui.objects.AppDrawer
import com.example.be.utilits.AUTH
import com.example.be.utilits.CURRENT_UID
import com.example.be.utilits.NODE_USERS
import com.example.be.utilits.REF_DATABASE_ROOT
import com.example.be.utilits.USER
import com.example.be.utilits.initFirebase
import com.example.be.utilits.replaceActivity
import com.example.be.utilits.replaceFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() { /*MainActivity копия класса AppCompatActivity()*/

    lateinit var mAppDrawer: AppDrawer
    lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    lateinit var bindingClass: ActivityMainBinding

    /*цикл жизни*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater) /*хранит все элементы экрана*/
        setContentView(bindingClass.root) /*рисует все элементы*/

        initFirebase()
        initFields()
        initFunc()

        if (AUTH.currentUser == null) replaceActivity(Registration())

    }

    private fun initFields() {
        APP_ACTIVITY = this
        mToolbar = bindingClass.mainToolbar
        mAppDrawer = AppDrawer(this, mToolbar)

        initUser()
    }

    private fun initUser() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("MyLog", "$snapshot")
                    USER = snapshot.getValue(User::class.java) ?: User()
                    Log.d("MyLog", "$USER")
                }

                override fun onCancelled(error: DatabaseError) {}

            })
    }

    private fun initFunc() {
        setSupportActionBar(mToolbar)
        mAppDrawer.create()
        replaceFragment(MainFragment())
    }





    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
    }


}