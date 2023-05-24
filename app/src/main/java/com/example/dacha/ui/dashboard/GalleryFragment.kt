package com.example.dacha.ui.dashboard

import android.content.ContentValues
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacha.R
import com.example.dacha.databinding.FragmentDashboardBinding
import com.example.dacha.databinding.FragmentGalleryBinding
import com.google.android.material.tabs.TabLayout.TabGravity
import com.squareup.picasso.Picasso
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.dto.common.id.UserId
import com.vk.sdk.api.photos.PhotosService
import com.vk.sdk.api.photos.dto.PhotosGetAlbumsResponse
import com.vk.sdk.api.photos.dto.PhotosGetResponse


class GalleryFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var albumId = ""
        val bundle = arguments
        if (bundle != null) {
            Log.e(ContentValues.TAG, bundle.getString("AlbumId").toString())
        }
        albumId = bundle?.getString("AlbumID") ?: "Error"



        VK.execute(
            PhotosService().photosGet(
                UserId(307517152), albumId, null, false, null, 0, false, null, null
            ), object :
                VKApiCallback<PhotosGetResponse> {
                override fun success(result: PhotosGetResponse) {
                    var url = result.items[0].sizes?.get(6)?.url
                    val listOfPhotos = mutableListOf<String>()
//                    textView.text = url.toString()
//                    Picasso.get().load(url).into(imageView)
                    result.items.forEach {
                        if (it.sizes != null) {
                            it.sizes!!.forEach { itPhoto ->
                                if (itPhoto.type.toString() == "Y"
                                    || itPhoto.type.toString() == "Z"
                                    || itPhoto.type.toString() == "W"
                                ) {
                                    url = itPhoto.url
                                }
                            }

                        }
                        listOfPhotos.add(url.toString())


                    }
                    binding.rcGallery.layoutManager = LinearLayoutManager(context)
                    binding.rcGallery.adapter = GalleryAdapter(listOfPhotos)
                    Log.e(ContentValues.TAG, listOfPhotos.toString())


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
//                Log.e(ContentValues.TAG, list.toString())

                }

                override fun fail(error: Exception) {
                    Log.e(ContentValues.TAG, error.toString())
                }
            })




        return root
//        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }


}