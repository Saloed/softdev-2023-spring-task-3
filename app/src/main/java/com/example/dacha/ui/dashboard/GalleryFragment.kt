package com.example.dacha.ui.dashboard

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.registerImagePicker
import com.example.dacha.R
import com.example.dacha.data.model.AlbumModel
import com.example.dacha.data.model.PersonModel
import com.example.dacha.databinding.FragmentGalleryBinding
import com.example.dacha.ui.home.HomeViewModel
import com.example.dacha.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private val viewModel: DashboardViewModel by viewModels()
    private val homeVM: HomeViewModel by viewModels()
    lateinit var binding: FragmentGalleryBinding
    var album: AlbumModel? = null
    var imageUris: MutableList<String> = arrayListOf()
    var person = PersonModel()
    val adapter by lazy {
        GalleryAdapter(onDeleteClicked = { photo -> onRemoveImage(photo) })
    }

    private val launcher = registerImagePicker { images ->
        uploadImages(images.map { it.uri })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)

        homeVM.getPerson()
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.hide()
        album = arguments?.getParcelable("album")

        binding.rcGallery.layoutManager = LinearLayoutManager(requireContext())
        binding.rcGallery.adapter = adapter

        imageUris = album?.photos?.toMutableList() ?: arrayListOf()
        adapter.updateList(imageUris)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddPhoto.setOnClickListener {
            launcher.launch(ImagePickerConfig {
                isFolderMode = true
                arrowColor = R.color.white
                imageTitle = getString(R.string.choose_photo)
                theme = R.style.Theme_Dacha
                folderTitle = getString(R.string.title_gallery)
                doneButtonText = getString(R.string.done)
            }
            )
        }
        binding.tvAlbumTop.text = album?.name
    }

    private fun uploadImages(images: List<Uri>) {
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
        viewModel.onUploadFiles(images) { state ->
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
                    imageUris.addAll(state.data.values.toList())
                    adapter.updateList(imageUris)
                    homeVM.addNews(
                        news(person, getString(R.string.add_photos, album?.name))
                    )
                    toast(getString(R.string.uploaded_photos))
                    if (album != null) {
                        viewModel.updateAlbum(AlbumModel(album!!.name, album!!.key, imageUris))
                    }
                }
            }
        }
    }

    private fun onRemoveImage(item: String) {
        imageUris.remove(item)
        adapter.updateList(imageUris)
        viewModel.updateAlbum(AlbumModel(album!!.name, album!!.key, imageUris))

        homeVM.addNews(
            news(person, getString(R.string.delete_photo, album?.name))
        )
        toast(getString(R.string.photo) + " " + getString(R.string.deleted))
    }
}