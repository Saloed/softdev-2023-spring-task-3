package com.example.dacha.ui.dashboard

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.dacha.R
import com.example.dacha.data.model.AlbumModel
import com.example.dacha.databinding.GalleryItemBinding
import com.squareup.picasso.Picasso

class GalleryAdapter(val onDeleteClicked: (Int, String) -> Unit):
    RecyclerView.Adapter<GalleryAdapter.GalleryHolder>() {

    private var list: MutableList<String> = arrayListOf()
    inner class GalleryHolder(item: View): RecyclerView.ViewHolder(item){
        private val binding = GalleryItemBinding.bind(item)
        fun bind(photo: String) {
            Picasso.get().load(photo).into(binding.iVP)
            binding.deletePhoto.setImageResource(R.drawable.baseline_clear_24)
            binding.deletePhoto.setOnClickListener {
                onDeleteClicked.invoke(bindingAdapterPosition, photo)
            }
        }
    }

    fun updateList(list: MutableList<String>) {
        this.list = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)
        return GalleryHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GalleryHolder, position: Int) {
        holder.bind(list[position])
    }
}