package com.example.dacha.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.dacha.R
import com.example.dacha.databinding.FragmentHomeBinding
import com.example.dacha.data.model.PersonModel
import com.example.dacha.utils.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
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

        val iVPoster: ImageView = binding.iVPoster
        var invisible = false

        viewModel.getPeople()
        observer()

        binding.addName.setOnClickListener {
            showAddPersonDialog()
        }

        binding.chooseName.setOnClickListener {
            showChoosePersonDialog()
        }

        binding.btnLogout.setOnClickListener {
            person?.let { it1 ->
                viewModel.logout(it1)
                updateUI(HomeStates.DELETE, "")
            }
        }


        Glide.with(this)
            .load(R.drawable.video_dacha)
            .into(binding.iVGif)
        binding.btnPoster.setOnClickListener {
            invisible = if (!invisible) {
                iVPoster.show()
                true
            } else {
                iVPoster.hide()
                false
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
                        updateUI(HomeStates.DELETE, "")
                    } else {
                        person = state.data
                        var name = person!!.name.toString()
                        if (list.isNotEmpty()) {
                            list.forEach {
                                if (it.id.toString() == person!!.id.toString()) name =
                                    it.name.toString()
                            }
                        }
                        updateUI(HomeStates.ADD, name)
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
                    list = state.data.toMutableList()
                }
                else -> {
                    binding.progressBar.show()
                }
            }
        }
    }

    private fun updateUI(state: HomeStates, data: String) {
        when (state) {
            HomeStates.ADD -> {
                binding.progressBar.hide()
                binding.btgLogin.hide()
                binding.tvGlobalName.show()
                binding.btnLogout.show()
                binding.tvGlobalName.text = data
            }
            HomeStates.CHOOSE -> {
                binding.progressBar.hide()
                binding.btgLogin.hide()
                binding.tvGlobalName.show()
                binding.btnLogout.show()
                binding.tvGlobalName.text = data
            }
            HomeStates.DELETE -> {
                binding.progressBar.hide()
                person = null
                binding.btgLogin.show()
                binding.tvGlobalName.hide()
                binding.btnLogout.hide()
            }
        }
    }

    private fun showAddPersonDialog() {
        val dialog = requireContext().createDialog(R.layout.add_person_dialog, true)
        val button = dialog.findViewById<MaterialButton>(R.id.name_dialog_btn)
        val editText = dialog.findViewById<EditText>(R.id.name_dialog_et)
        button.setOnClickListener {
            if (editText.text.toString().isEmpty()) {
                toast(getString(R.string.empty_field))
            } else {
                val text = editText.text.toString()
                viewModel.addPerson(text)
                dialog.dismiss()
                updateUI(HomeStates.ADD, text)
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
        val dialog =
            MaterialAlertDialogBuilder(this.requireContext(), R.layout.choose_person_dialog)
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
            updateUI(HomeStates.CHOOSE, newPerson.name.toString())
        }
    }

    enum class HomeStates {
        ADD, CHOOSE, DELETE
    }
}