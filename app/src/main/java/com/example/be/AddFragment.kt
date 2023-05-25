package com.example.be

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.be.databinding.FragmentAddBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass.
 * Use the [AddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFragment : DialogFragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var auth: FirebaseAuth
    /*private lateinit var listener: DialogNameFolderBtnClickListeners*/



    /*fun setListener(listener: DialogNameFolderBtnClickListeners) {
        this.listener = listener
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerEvents()
    }

    private fun registerEvents() {
        binding.btnReateFolder.setOnClickListener {
            val nameFolder = binding.etFolderName.text.toString()
            if (nameFolder.isNotEmpty()) {
                /*listener.onSaveTask(nameFolder, binding.etFolderName)*/
                /*Folder(R.drawable.baseline_folder_24, nameFolder)*/

                dismiss()
                Log.d("MyLog", "Before addFolder")
                /*MainActivity().addFolder()*/
            } else {
                Toast.makeText(context, "Напишите название папки", Toast.LENGTH_SHORT).show()
            }
        }


    }



}