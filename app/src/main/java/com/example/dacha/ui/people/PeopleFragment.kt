package com.example.dacha.ui.people

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacha.data.model.PersonModel
import com.example.dacha.databinding.FragmentPeopleBinding
import com.example.dacha.utils.UiState
import com.example.dacha.utils.hide
import com.example.dacha.utils.show
import com.example.dacha.utils.toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class PeopleFragment : Fragment() {

    private val viewModel: PeopleViewModel by viewModels()
    var deleteItemPos = -1
    lateinit var binding: FragmentPeopleBinding
    val adapter by lazy {
        PeopleAdapter(onDeleteClicked = { pos, item -> onDeleteClicked(pos, item) },
            onPersonClicked = { pos, item -> onPersonClicked(pos, item) })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            val personBottomFragment = PersonBottomFragment()
            personBottomFragment.setDismissListener {
                if (it) {
                    viewModel.getPeople()
                }
            }
            personBottomFragment.show(childFragmentManager, "add person")
        }

        binding.rcPeople.layoutManager = LinearLayoutManager(requireContext())
        binding.rcPeople.adapter = adapter
        viewModel.getPeople()
        observer()
    }

    private fun observer() {
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
                    adapter.updateList(state.data.toMutableList())
                }
                else -> {
                    binding.progressBar.show()
                }
            }
        }
        viewModel.deletePerson.observe(viewLifecycleOwner) { state ->
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
                    if (deleteItemPos > -1) {
                        toast(state.data.second)
                        adapter.removeItem(deleteItemPos)
                    }
                }
                else -> {
                    binding.progressBar.show()
                }
            }
        }
    }

    private fun onDeleteClicked(pos: Int, item: PersonModel) {
        deleteItemPos = pos
        viewModel.deletePerson(item)
    }

    private fun onPersonClicked(pos: Int, item: PersonModel) {
        val personBottomFragment = PersonBottomFragment(item)
        personBottomFragment.setDismissListener {
            if (it) {
                viewModel.getPeople()
            }
        }
        personBottomFragment.show(childFragmentManager, "change person")
    }
}
