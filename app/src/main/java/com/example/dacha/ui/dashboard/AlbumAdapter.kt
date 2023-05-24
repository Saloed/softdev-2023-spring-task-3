package com.example.dacha.ui.dashboard

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dacha.MainActivity
import com.example.dacha.R
import com.example.dacha.databinding.AlbumItemBinding
import com.example.dacha.databinding.AlbumItemBinding.*
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation
import jp.wasabeef.picasso.transformations.ColorFilterTransformation
import jp.wasabeef.picasso.transformations.GrayscaleTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation


class AlbumAdapter(private val albumList: MutableList<Album>, private val click: Click): RecyclerView.Adapter<AlbumAdapter.AlbumHolder>() {
    class AlbumHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = bind(item)
        fun bind(album: Album) {
            binding.tVAlbum.text = album.title
            val transformation = RoundedCornersTransformation(12, 0)
            Picasso
                .get()
                .load(album.cover)
                .resize(300, 100)
                .centerCrop()
                .transform(transformation)
                .into(binding.iVCover)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.album_item, parent, false)
        return AlbumHolder(view)
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener {
            click.sendData(albumList[position].albumId)
            Log.e(ContentValues.TAG, albumList[position].albumId)
        }
    }

}
