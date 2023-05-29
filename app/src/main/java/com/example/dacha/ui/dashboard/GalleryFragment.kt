package com.example.dacha.ui.dashboard

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.registerImagePicker
import com.example.dacha.R
import com.example.dacha.data.model.AlbumModel
import com.example.dacha.data.model.NewsModel
import com.example.dacha.data.model.PersonModel
import com.example.dacha.databinding.FragmentGalleryBinding
import com.example.dacha.ui.home.HomeViewModel
import com.example.dacha.utils.UiState
import com.example.dacha.utils.hide
import com.example.dacha.utils.show
import com.example.dacha.utils.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private val viewModel: DashboardViewModel by viewModels()
    val homeVM: HomeViewModel by viewModels()
    lateinit var binding: FragmentGalleryBinding
    var album: AlbumModel? = null
    var imageUris: MutableList<String> = arrayListOf()
    var person = PersonModel()
    val adapter by lazy {
        GalleryAdapter(onDeleteClicked = { pos, photo -> onRemoveImage(pos, photo) })
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
        if (activity == null) Log.e("NULL", "NULL")
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
                arrowColor = R.color.new_status_bar
                folderTitle = "Галерея"
                imageTitle = "Выберите фото"
                doneButtonText = "Готово"
            }
            )
        }
        binding.tvAlbumTop.text = album?.name
    }

    private fun uploadImages(images: List<Uri>) {
        homeVM.person.observe(viewLifecycleOwner){ state ->
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
                    person = state.data!!
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
                        NewsModel(
                            null,
                            person,
                            "Обновил(а) фотографии в альбоме ${album?.name}",
                            LocalDateTime.now().toString().split(".")[0]
                        )
                    )
                    if (album != null) {
                        viewModel.updateAlbum(AlbumModel(album!!.name, album!!.key, imageUris))
                    }
                }
            }
        }
    }

    private fun onRemoveImage(pos: Int, item: String) {
        imageUris.remove(item)
        adapter.updateList(imageUris)
        viewModel.updateAlbum(AlbumModel(album!!.name, album!!.key, imageUris))

        homeVM.addNews(
            NewsModel(
                null,
                person,
                "Удалил(а) фотографию в альбоме ${album?.name}",
                LocalDateTime.now().toString().split(".")[0]
            )
        )
    }
}