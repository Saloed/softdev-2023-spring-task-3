package com.example.dacha.data.repository

import android.net.Uri
import com.example.dacha.data.model.AlbumModel
import com.example.dacha.utils.UiState

interface DashboardRepository {
    suspend fun getAlbums(): UiState<List<AlbumModel>>
    fun addAlbum(album: AlbumModel, result: (UiState<AlbumModel>) -> Unit)
    fun updateAlbum(album: AlbumModel, result: (UiState<AlbumModel>) -> Unit)
    fun deleteAlbum(album: AlbumModel, result: (UiState<AlbumModel>) -> Unit)
    suspend fun uploadFiles(fileUri: List<Uri>, onResult: (UiState<Map<Uri, String>>) -> Unit)
}