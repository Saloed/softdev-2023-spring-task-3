package com.example.dacha.ui.dashboard

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacha.R
import com.example.dacha.databinding.FragmentDashboardBinding
import com.example.dacha.databinding.FragmentGalleryBinding
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.dto.common.id.UserId
import com.vk.sdk.api.photos.PhotosService
import com.vk.sdk.api.photos.dto.PhotosGetAlbumsResponse


class DashboardFragment : Fragment(), Click {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val list = mutableListOf<Album>()
    lateinit var click: Click

    override fun sendData(albumId: String) {
        val fragment = GalleryFragment()
        val bundle = Bundle()
        bundle.putString("AlbumID", albumId)
        fragment.arguments = bundle
        childFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_dashboard, fragment)
            .addToBackStack(null)
            .commit()
        bundle.getString("AlbumID")?.let { Log.e(TAG, it) }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        click = this

        VK.execute(PhotosService().photosGetAlbums(
            UserId(307517152), null, null, null, false,
            needCovers = true,
            photoSizes = false
        ), object :
            VKApiCallback<PhotosGetAlbumsResponse> {
            override fun success(result: PhotosGetAlbumsResponse) {
                for (i in 0 until result.count) {
                    list.add(
                        Album(
                            result.items[i].title,
                            result.items[i].id.toString(),
                            result.items[i].thumbSrc.toString()
                        )
                    )
                }
                binding.rcView.layoutManager = LinearLayoutManager(context)
                binding.rcView.adapter = AlbumAdapter(list, click)
                Log.e(TAG, list.toString())

            }
            override fun fail(error: Exception) {
                Log.e(TAG, error.toString())
            }
        })

        binding.apply {
            Log.e(TAG, list.toString())
        }



            //init()


            return root

    }
//    private fun init() {
//        binding.apply {
//          rcView.layoutManager = LinearLayoutManager(context)
//          Log.e(TAG, list.toString())
//          rcView.adapter = AlbumAdapter(list)
//
//
//        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    val html =
        "https://api.vk.com/method/photos.getAlbums?owner_id=307517152&access_token=vk1.a.b55w9s4cdK1Ln-Hpdnad0SIqSxuZdg13WlE1clMp17563cUnIxz6my7ywl-obz_-7PhK4tjxB4i93y4x6cBpj32S-v2I3kKIoes7L4XWzYe7ReHhzy9CYYjfwNChzqnMPe7SxyoVSquVHnezAiXDdyqiMJHOxjFUUsF4VFjhH1BYnjXPsyrCueMrYvc1keVX&need_covers=1&v=5.131"
}