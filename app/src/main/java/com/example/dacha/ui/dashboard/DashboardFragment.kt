package com.example.dacha.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacha.R
import com.example.dacha.data.model.AlbumModel
import com.example.dacha.data.model.PersonModel
import com.example.dacha.databinding.FragmentDashboardBinding
import com.example.dacha.ui.home.HomeViewModel
import com.example.dacha.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private val viewModel: DashboardViewModel by viewModels()
    private val homeVM: HomeViewModel by viewModels()
    var person = PersonModel()
    lateinit var binding: FragmentDashboardBinding

    val adapter by lazy {
        AlbumAdapter(onAlbumClicked = { album ->
            findNavController().navigate(
                R.id.action_dashboardFragment_to_galleryFragment,
                Bundle().apply { putParcelable("album", album) })
        }, onAlbumLongClicked = { album -> showUpdateAlbumDialog(album) })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.show()
        viewModel.getAlbums()
        homeVM.getPerson()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcView.layoutManager = LinearLayoutManager(requireContext())
        binding.rcView.adapter = adapter
        binding.btnAddAlbum.setOnClickListener {
            showAddAlbumDialog()
        }
        observer()

    }

    private fun showAddAlbumDialog() {
        val dialog = requireContext().createDialog(R.layout.add_person_dialog, true)
        val button = dialog.findViewById<MaterialButton>(R.id.name_dialog_btn)
        val editText = dialog.findViewById<EditText>(R.id.name_dialog_et)
        button.setOnClickListener {
            if (editText.text.toString().isEmpty()) {
                toast(getString(R.string.empty_field))
            } else {
                val text = editText.text.toString()
                viewModel.addAlbum(AlbumModel(text, null, emptyList()))
                homeVM.addNews(
                    news(person, "${getString(R.string.add_album)} $text")
                )
                toast(getString(R.string.album) + " " + getString(R.string.added))
                viewModel.getAlbums()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun showUpdateAlbumDialog(album: AlbumModel) {
        val dialog = requireContext().createDialog(R.layout.change_album_dialog, true)
        val button = dialog.findViewById<MaterialButton>(R.id.album_dialog_btn)
        val delete = dialog.findViewById<MaterialButton>(R.id.delete_album_btn)
        val editText = dialog.findViewById<EditText>(R.id.album_dialog_et)
        editText.hint = album.name.toString()
        button.text = getString(R.string.to_change)
        delete.setOnClickListener {
            viewModel.deleteAlbum(album)
            homeVM.addNews(
                news(person, "${getString(R.string.delete_album)}${album.name}")
            )
            toast(getString(R.string.album) + " " + getString(R.string.deleted))
            viewModel.getAlbums()
            dialog.dismiss()
        }
        button.setOnClickListener {
            if (editText.text.toString().isEmpty()) {
                toast(getString(R.string.empty_field))
            } else {
                val text = editText.text.toString()
                viewModel.updateAlbum(AlbumModel(text, album.key, album.photos))
                homeVM.addNews(
                    news(person, "${getString(R.string.update_album)} $text")
                )
                toast(getString(R.string.album) + " " + getString(R.string.updated))
                viewModel.getAlbums()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun observer() {
        viewModel.albums.observe(viewLifecycleOwner) { state ->
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
            }
        }
        homeVM.person.observe(viewLifecycleOwner) { state ->
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
                    if (state.data == null) toast(getString(R.string.go_login))
                    else person = state.data
                }
            }
        }
    }
}