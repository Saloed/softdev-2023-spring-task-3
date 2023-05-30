package com.example.be.ui.fragments

import android.app.Dialog
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.be.R
import com.example.be.utilits.APP_ACTIVITY
import com.example.be.utilits.COUNT_SNAPSHOT_PLUS
import com.example.be.activity.Registration
import com.example.be.models.Folder
import com.example.be.ui.fragments.adapters.FolderAdapter
import com.example.be.ui.fragments.changes.ChangeFolderNameFragment
import com.example.be.utilits.AUTH
import com.example.be.utilits.CHILD_FOLDERS
import com.example.be.utilits.CURRENT_UID
import com.example.be.utilits.NODE_USERS
import com.example.be.utilits.REF_DATABASE_ROOT
import com.example.be.utilits.countSnapshot
import com.example.be.utilits.replaceActivity
import com.example.be.utilits.replaceFragment
import com.example.be.utilits.showToast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


class MainFragment : Fragment(R.layout.fragment_main), FolderAdapter.OnItemClickListener {

    private lateinit var rcForFolderView: RecyclerView
    private lateinit var cardPlus: CardView
    private lateinit var databaseReference: DatabaseReference

    private lateinit var adapter: FolderAdapter
    private lateinit var nameFolderList: ArrayList<Folder>
    private lateinit var data: ArrayList<Folder>

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
        initFuns()
    }

    private fun initFuns() {
        fetchData()
        registerEvents()
    }

    private fun initFields() {
        data = ArrayList()
        nameFolderList = arrayListOf()
        adapter = FolderAdapter(data, this@MainFragment)

        databaseReference =
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FOLDERS)

        cardPlus = view?.findViewById(R.id.cardViewPlus)!!
        rcForFolderView = view?.findViewById(R.id.rcForFolder)!!

    }

    private fun registerEvents() {
        /*добавление папки*/
        cardPlus.setOnClickListener {
            dialogCreateNewFolder()
        }
    }

    private fun fetchData() {
        COUNT_SNAPSHOT_PLUS = 0
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                nameFolderList.clear()
                if (snapshot.exists()) {
                    rcForFolderView.visibility = View.VISIBLE
                    view?.findViewById<TextView>(R.id.textView2)?.visibility = View.GONE
                    for (s in snapshot.children) {
                        val values = s.getValue(Folder::class.java)
                        nameFolderList.add(values!!)
                        countSnapshot(values)
                    }
                    val mAdapter = FolderAdapter(nameFolderList, this@MainFragment)
                    rcForFolderView.layoutManager = GridLayoutManager(APP_ACTIVITY, 2)
                    rcForFolderView.adapter = mAdapter
                } else {
                    rcForFolderView.visibility = View.INVISIBLE
                    view?.findViewById<TextView>(R.id.textView2)?.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun dialogCreateNewFolder() {
        val dialog = Dialog(APP_ACTIVITY)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.fragment_add)
        val folderName = dialog.findViewById<EditText>(R.id.etFolderName)
        dialog.findViewById<Button>(R.id.btnСreateFolder).setOnClickListener {
            if (folderName.text.isEmpty()) {
                showToast("Заполните поле ввода")
            } else {
                val name = folderName.text.toString()
                val keyFolder = databaseReference.push().key.toString()
                data.add(Folder(name, keyFolder))

                databaseReference.child(keyFolder).setValue(Folder(name, keyFolder))
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            showToast("Папка сохранена")
                        }
                    }
                view?.findViewById<TextView>(R.id.textView2)?.visibility = View.GONE
                dialog.dismiss()
            }
        }

        dialog.findViewById<ImageView>(R.id.closeBtn).setOnClickListener {
            dialog.dismiss()
        }
        dialog.create()
        dialog.show()
    }

    override fun onFolderClick(folder: Folder) {
        /*FOLDER = Folder(folder.name, folder.id)*/
        replaceFragment(InFolderFragment())
    }

    override fun onDeleteClick(folder: Folder) {
        databaseReference.child(folder.id).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                showToast("Удалено")
            } else {
                showToast("Не Удалено")
            }
        }
    }

    override fun onEditClick(folder: Folder) {
        replaceFragment(ChangeFolderNameFragment())
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean { /*функция запускается когда нажимаем на какой-нибудь элемент из меню*/
        when (item.itemId) {
            R.id.menu_exit -> {
                AUTH.signOut()
                replaceActivity(Registration())
            }

            R.id.profile -> {
                replaceFragment(ProfileFragment())
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

}


