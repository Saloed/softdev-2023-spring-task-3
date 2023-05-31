package com.example.dacha.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumModel(
    var name: String? = null,
    var key: String? = null,
    var photos: List<String> = arrayListOf()
) : Parcelable
