package com.example.dacha.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dacha.R
import com.example.dacha.databinding.GalleryItemBinding
import com.squareup.picasso.Picasso

class GalleryAdapter(private val photoList: List<String>):
    RecyclerView.Adapter<GalleryAdapter.GalleryHolder>() {
    class GalleryHolder(item: View): RecyclerView.ViewHolder(item){
        private val binding = GalleryItemBinding.bind(item)
        fun bind(photo: String) {
            Picasso.get().load(photo).into(binding.iVP)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)
        return GalleryHolder(view)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onBindViewHolder(holder: GalleryHolder, position: Int) {
        holder.bind(photoList[position])
    }
}