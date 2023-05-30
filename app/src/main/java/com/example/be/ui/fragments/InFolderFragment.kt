package com.example.be.ui.fragments

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.be.R
import com.example.be.utilits.APP_ACTIVITY
import com.example.be.utilits.COUNT_SNAPSHOT
import com.example.be.ui.fragments.adapters.InFolderAdapter
import com.example.be.utilits.CHILD_FOLDERS
import com.example.be.utilits.CURRENT_UID
import com.example.be.utilits.FOLDER
import com.example.be.utilits.NODE_USERS
import com.example.be.utilits.REF_DATABASE_ROOT
import com.example.be.utilits.replaceFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.example.be.models.Message
import com.example.be.ui.fragments.changes.ChangeTitleMessageFragment
import com.example.be.ui.fragments.voice.VoiceMessageFragment
import com.example.be.utilits.TYPE_TEXT
import com.example.be.utilits.TYPE_VOICE
import com.example.be.utilits.showToast


class InFolderFragment : BaseFragment(R.layout.fragment_in_folder), InFolderAdapter.OnItemClickListener {
    private lateinit var nameFolder: TextView
    private lateinit var refMessages: DatabaseReference
    private lateinit var adapter: InFolderAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var messagesListener: ValueEventListener
    private var listMessage = ArrayList<Message>()



    override fun onStart() {
        super.onStart()
        initFields()
        initFunc()
    }

    private fun initFields() {
        nameFolder = binding.findViewById(R.id.nameOfFolder)/*view?.findViewById(R.id.nameOfFolder)!!*/
        val name: String = FOLDER.name
        val idFolder: String = FOLDER.id
        nameFolder.text = name

    }


    private fun initFunc() {
        initRecyclerView()
        registerEvents()
    }

    private fun initRecyclerView() {
        recyclerView = binding.findViewById(R.id.recyclerViewForMessages)
        adapter = InFolderAdapter(this)
        refMessages = REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(CURRENT_UID)
            .child(CHILD_FOLDERS)
            .child(FOLDER.id)
            /*.child(MESSAGE.id)*/
        Log.d("MyLog", "$refMessages")


        refMessages.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listMessage.clear()
                if (snapshot.exists()) {
                    Log.d("MyLog", "snapshot.exists()")
                    for(s in snapshot.children) {
                        if (s.key != "id" && s.key != "name") {
                            val value = s.getValue(Message::class.java)
                            listMessage.add(value!!)
                        }

                    }
                    recyclerView.layoutManager = LinearLayoutManager(APP_ACTIVITY)
                    recyclerView.adapter = adapter
                    COUNT_SNAPSHOT = listMessage.size
                    adapter.setList(listMessage)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }


    private fun registerEvents() {
        binding.findViewById<ImageView>(R.id.addMessage).setOnClickListener {
            replaceFragment(CreateMessageFragment())
        }
    }

    override fun onFolderClick(message: Message) {
        if (message.type == TYPE_TEXT){
            replaceFragment(InTextMessageFragment())
        } else if (message.type == TYPE_VOICE) {
            replaceFragment(VoiceMessageFragment())
        }
    }

    override fun onDeleteClick(message: Message) {
        refMessages.child(message.id).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                /*COUNT_MESSAGE -= 1*/
                showToast("Удалено")
            } else {
                showToast("Не Удалено")
            }
        }
    }

    override fun onEditClick(message: Message) {
        replaceFragment(ChangeTitleMessageFragment())
    }

}