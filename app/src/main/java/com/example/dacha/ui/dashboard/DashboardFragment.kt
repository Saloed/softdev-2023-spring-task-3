package com.example.dacha.ui.dashboard

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacha.R
import com.example.dacha.data.model.AlbumModel
import com.example.dacha.databinding.FragmentDashboardBinding
import com.example.dacha.databinding.FragmentGalleryBinding
import com.example.dacha.databinding.FragmentPeopleBinding
import com.example.dacha.ui.people.PeopleViewModel
import com.example.dacha.utils.*
import com.google.android.material.button.MaterialButton
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.dto.common.id.UserId
import com.vk.sdk.api.photos.PhotosService
import com.vk.sdk.api.photos.dto.PhotosGetAlbumsResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private val viewModel: DashboardViewModel by viewModels()
    lateinit var binding: FragmentDashboardBinding

    //val list = mutableListOf<Album>()
//    lateinit var click: Click


    val adapter by lazy {
        AlbumAdapter(onAlbumClicked = { pos, album ->
            findNavController().navigate(
                R.id.action_dashboardFragment_to_galleryFragment ,
                Bundle().apply { putParcelable("album", album) })
        }, onAlbumLongClicked = {pos, album -> showUpdateAlbumDialog(album)})
    }


//    override fun sendData(albumId: String) {
//        val fragment = GalleryFragment()
//        val bundle = Bundle()
//        bundle.putString("AlbumID", albumId)
//        fragment.arguments = bundle
//        childFragmentManager
//            .beginTransaction()
//            .replace(R.id.fragment_dashboard, fragment)
//            .addToBackStack(null)
//            .commit()
//        bundle.getString("AlbumID")?.let { Log.e(TAG, it) }
//    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        viewModel.getAlbums()
//        click = this
//
//        VK.execute(PhotosService().photosGetAlbums(
//            UserId(307517152), null, null, null, false,
//            needCovers = true,
//            photoSizes = false
//        ), object :
//            VKApiCallback<PhotosGetAlbumsResponse> {
//            override fun success(result: PhotosGetAlbumsResponse) {
//                for (i in 0 until result.count) {
//                    list.add(
//                        Album(
//                            result.items[i].title,
//                            result.items[i].id.toString(),
//                            result.items[i].thumbSrc.toString()
//                        )
//                    )
//                }
//                binding.rcView.layoutManager = LinearLayoutManager(context)
//                binding.rcView.adapter = AlbumAdapter(list, click)
//                Log.e(TAG, list.toString())
//
//            }
//            override fun fail(error: Exception) {
//                Log.e(TAG, error.toString())
//            }
//        })
//
//        binding.apply {
//            Log.e(TAG, list.toString())
//        }
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
        editText.hint = "Введите название альбома"
        button.setOnClickListener {
            if (editText.text.toString().isNullOrEmpty()) {
                toast("Введите название альбома")
            } else {
                val text = editText.text.toString()
                viewModel.addAlbum(AlbumModel(text, null, emptyList()))
                viewModel.getAlbums()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun showUpdateAlbumDialog(album: AlbumModel) {
        val dialog = requireContext().createDialog(R.layout.add_person_dialog, true)
        val button = dialog.findViewById<MaterialButton>(R.id.name_dialog_btn)
        val editText = dialog.findViewById<EditText>(R.id.name_dialog_et)
        editText.hint = album.name.toString()
        button.setOnClickListener {
            if (editText.text.toString().isNullOrEmpty()) {
                toast("Введите название альбома")
            } else {
                val text = editText.text.toString()
                viewModel.updateAlbum(AlbumModel(text, album.key, album.photos))
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
    }
}