package com.example.dacha.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dacha.R
import com.example.dacha.data.model.AlbumModel
import com.example.dacha.databinding.AlbumItemBinding.*
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation


class AlbumAdapter(
    val onAlbumClicked: (AlbumModel) -> Unit,
    val onAlbumLongClicked: (AlbumModel) -> Unit
) :
    RecyclerView.Adapter<AlbumAdapter.AlbumHolder>() {

    private var list: MutableList<AlbumModel> = arrayListOf()

    inner class AlbumHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = bind(item)
        fun bind(album: AlbumModel) {
            binding.tVAlbum.text = album.name
            val transformation = RoundedCornersTransformation(12, 0)
            if (album.photos.isNotEmpty()) {
                Picasso
                    .get()
                    .load(album.photos[0])
                    .resize(300, 100)
                    .centerCrop()
                    .transform(transformation)
                    .into(binding.iVCover)
            }
            binding.cVAlbum.setOnClickListener {
                onAlbumClicked.invoke(
                    album
                )
            }
            binding.cVAlbum.setOnLongClickListener {
                onAlbumLongClicked.invoke(
                    album
                )
                true
            }
        }
    }

    fun updateList(list: MutableList<AlbumModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.album_item, parent, false)
        return AlbumHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
        holder.bind(list[position])
    }

}
