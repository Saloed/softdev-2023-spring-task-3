package com.example.dacha.data.repository

import android.net.Uri
import com.example.dacha.data.model.AlbumModel
import com.example.dacha.utils.UiState

interface DashboardRepository {
    fun getAlbums(result: (UiState<List<AlbumModel>>) -> Unit)
    fun addAlbum(album: AlbumModel, result: (UiState<Pair<AlbumModel,String>>) -> Unit)
    fun updateAlbum(album: AlbumModel, result: (UiState<Pair<AlbumModel, String>>) -> Unit)
    fun deleteAlbum(album: AlbumModel, result: (UiState<Pair<AlbumModel, String>>) -> Unit)
    suspend fun uploadFiles(fileUri: List<Uri>, onResult: (UiState<Map<Uri, String>>) -> Unit)
}