package com.example.dacha.ui.dashboard

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacha.data.model.AlbumModel
import com.example.dacha.data.repository.DashboardRepository
import com.example.dacha.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(val repository: DashboardRepository) : ViewModel() {
    private val _addAlbum = MutableLiveData<UiState<AlbumModel>>()
    val addAlbum: LiveData<UiState<AlbumModel>> = _addAlbum

    fun addAlbum(album: AlbumModel) {
        _addAlbum.value = UiState.Loading
        repository.addAlbum(album) { _addAlbum.value = it }
    }

    private val _updateAlbum = MutableLiveData<UiState<AlbumModel>>()
    val updateAlbum: LiveData<UiState<AlbumModel>> = _updateAlbum

    fun updateAlbum(album: AlbumModel) {
        _updateAlbum.value = UiState.Loading
        repository.updateAlbum(album) { _updateAlbum.value = it }
    }

    private val _deleteAlbum = MutableLiveData<UiState<AlbumModel>>()
    val deleteAlbum: LiveData<UiState<AlbumModel>> = _deleteAlbum

    fun deleteAlbum(album: AlbumModel) {
        _deleteAlbum.value = UiState.Loading
        repository.deleteAlbum(album) { _deleteAlbum.value = it }
    }

    private val _albums = MutableLiveData<UiState<List<AlbumModel>>>()
    val albums: LiveData<UiState<List<AlbumModel>>> = _albums

    fun getAlbums() = viewModelScope.launch {
        _albums.value = UiState.Loading
        val result = repository.getAlbums()
        _albums.value = result
    }

    fun onUploadFiles(fileUris: List<Uri>, onResult: (UiState<Map<Uri, String>>) -> Unit) {
        onResult.invoke(UiState.Loading)
        viewModelScope.launch {
            repository.uploadFiles(fileUris, onResult)
        }
    }
}