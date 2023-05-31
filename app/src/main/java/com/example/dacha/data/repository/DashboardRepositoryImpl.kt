package com.example.dacha.data.repository

import android.net.Uri
import com.example.dacha.data.model.AlbumModel
import com.example.dacha.utils.FireDatabase
import com.example.dacha.utils.UiState
import com.google.firebase.FirebaseException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class DashboardRepositoryImpl(
    val database: FirebaseDatabase,
    private val storageReference: StorageReference
) : DashboardRepository {

    override suspend fun getAlbums(): UiState<List<AlbumModel>> {
        val ref = database.reference.child(FireDatabase.GALLERY)
        val albums = arrayListOf<AlbumModel>()
        ref.get()
            .addOnSuccessListener {
                for (item in it.children) {
                    val album = item.getValue(AlbumModel::class.java)
                    if (album != null) albums.add(album)
                }
            }.await()
        return UiState.Success(albums)
    }

    override fun addAlbum(album: AlbumModel, result: (UiState<AlbumModel>) -> Unit) {
        val ref = database.reference.child(FireDatabase.GALLERY).push()
        val uniqueKey = ref.key ?: "invalid"
        album.key = uniqueKey
        ref
            .setValue(album)
            .addOnSuccessListener {
                result.invoke(UiState.Success(album))
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override fun updateAlbum(
        album: AlbumModel,
        result: (UiState<AlbumModel>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.GALLERY).child(album.key.toString())
        ref
            .setValue(album)
            .addOnSuccessListener {
                result(UiState.Success(album))
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override fun deleteAlbum(
        album: AlbumModel,
        result: (UiState<AlbumModel>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.GALLERY).child(album.key.toString())
        ref
            .removeValue()
            .addOnSuccessListener {
                result(UiState.Success(album))
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override suspend fun uploadFiles(
        fileUri: List<Uri>,
        onResult: (UiState<Map<Uri, String>>) -> Unit
    ) {
        try {
            val urls = mutableMapOf<Uri, String>()
            withContext(Dispatchers.IO) {
                fileUri.map { image ->
                    async {
                        storageReference
                            .child(image.toString())
                            .putFile(image)
                            .await()
                            .storage
                            .downloadUrl
                            .addOnSuccessListener {
                                urls[image] = it.toString()
                            }
                            .await()
                    }
                }.awaitAll()
            }
            onResult(UiState.Success(urls))
        } catch (e: FirebaseException) {
            onResult(UiState.Failure(e.message))
        } catch (e: Exception) {
            onResult(UiState.Failure(e.message))
        }
    }
}