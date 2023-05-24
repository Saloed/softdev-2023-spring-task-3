package com.example.dacha.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.dacha.R
import com.example.dacha.databinding.FragmentHomeBinding
import com.example.dacha.data.model.PersonModel
import com.example.dacha.utils.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()


    val helloText = "Добро\nпожаловать\nна дачу!"

    private var list = mutableListOf<PersonModel>()
    private var person: PersonModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel.getPerson()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val btnAddName: Button = binding.addName
        val btnChooseName: Button = binding.chooseName
        val textView: TextView = binding.textHome
        val iVGif: ImageView = binding.iVGif
        val iVPoster: ImageView = binding.iVPoster
        val btgLogin: MaterialButtonToggleGroup = binding.btgLogin
        iVPoster.visibility = View.INVISIBLE
        val btnPoster: Button = binding.btnPoster
        var invisible = false
        btgLogin.visibility = View.GONE
        val btnLogout: Button = binding.btnLogout
        btnLogout.visibility = View.GONE



        textView.text = helloText

        btnAddName.setOnClickListener {
            showAddPersonDialog()
        }

        btnLogout.setOnClickListener {
            person?.let {
                    it1 -> viewModel.logout(it1)
            updateUI("logout", "")}
        }

        btnChooseName.setOnClickListener {
            showChoosePersonDialog()
        }

        viewModel.getPeople()


        observer()


        Glide.with(this)
            .load(R.drawable.video_dacha)
            .into(iVGif)
        btnPoster.setOnClickListener {
            if (!invisible) {
                iVPoster.visibility = View.VISIBLE
                invisible = true
            } else {
                iVPoster.visibility = View.INVISIBLE
                invisible = false
            }
        }
    }



    private fun observer() {
        viewModel.person.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    if (state.data == null) {
                        person = null
                        updateUI("logout", "")
                    } else {
                        person = state.data
                        var name = person!!.name.toString()
                        if (list.isNotEmpty()) {
                            list.forEach {
                                if (it.id.toString() == person!!.id.toString()) name = it.name.toString()
                            }
                        }
                        updateUI("add", name)
                    }
                }
                else -> {
                    binding.progressBar.show()
                }
            }
        }

        viewModel.people.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    updatePeople(state.data)
                }
                else -> {
                    binding.progressBar.show()
                }
            }
        }
    }

    private fun updateUI(state: String, data: String) {
        when(state) {
            "add" -> {
                binding.progressBar.hide()
                    binding.btgLogin.visibility = View.GONE
                    binding.tvGlobalName.visibility = View.VISIBLE
                    binding.btnLogout.visibility = View.VISIBLE
                    binding.tvGlobalName.text = data
            }
            "choose" -> {
                    binding.progressBar.hide()
                    binding.btgLogin.visibility = View.GONE
                    binding.tvGlobalName.visibility = View.VISIBLE
                    binding.btnLogout.visibility = View.VISIBLE
                    binding.tvGlobalName.text = data
            }
            "logout" -> {
                binding.progressBar.hide()
                person = null
                binding.btgLogin.visibility = View.VISIBLE
                binding.tvGlobalName.visibility = View.GONE
                binding.btnLogout.visibility = View.GONE
            }
        }
    }
    private fun updatePeople(list: List<PersonModel>) {
        this.list = list.toMutableList()
    }

    private fun showAddPersonDialog() {
        val dialog = requireContext().createDialog(R.layout.add_person_dialog, true)
        val button = dialog.findViewById<MaterialButton>(R.id.name_dialog_btn)
        val editText = dialog.findViewById<EditText>(R.id.name_dialog_et)
        button.setOnClickListener {
            if (editText.text.toString().isNullOrEmpty()) {
                toast("Введите имя")
            } else {
                val text = editText.text.toString()
                viewModel.addPerson(text)
                dialog.dismiss()
                updateUI("add", text)
            }
        }
        dialog.show()

    }

    private fun showChoosePersonDialog() {
        val keys = mutableMapOf<String, String>()
        list.forEach {
            keys[it.name.toString()] = it.id.toString()
        }
        var newPerson = list[0]
        val dialog = MaterialAlertDialogBuilder(this.requireContext(), R.layout.choose_person_dialog)
            .setSingleChoiceItems(keys.keys.toTypedArray(), 0) { _, i ->
                list.forEach {
                    if (it.id.toString() == keys.values.elementAt(i)) newPerson = it
                }
            }
            .setView(R.layout.choose_person_dialog)
            .create()
        dialog.show()
        dialog.findViewById<MaterialButton>(R.id.name_dialog_btn)?.setOnClickListener {
            viewModel.choosePerson(newPerson)
            dialog.dismiss()
            updateUI("choose", newPerson.name.toString())
        }

    }

}