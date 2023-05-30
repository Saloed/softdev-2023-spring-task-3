package com.example.dacha.data.repository

import android.net.Uri
import android.util.Log
import com.example.dacha.data.model.AlbumModel
import com.example.dacha.data.model.PersonModel
import com.example.dacha.utils.FireDatabase
import com.example.dacha.utils.UiState
import com.google.firebase.FirebaseException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DashboardRepositoryImpl(
    val database: FirebaseDatabase,
    val storageReference: StorageReference
) : DashboardRepository {
    override fun getAlbums(result: (UiState<List<AlbumModel>>) -> Unit) {
        val ref = database.reference.child(FireDatabase.GALLERY)
        ref.get()
            .addOnSuccessListener {
                val albums = arrayListOf<AlbumModel>()
                for (item in it.children) {
                    val album = item.getValue(AlbumModel::class.java)
                    if (album != null) albums.add(album)
                }
                result.invoke(UiState.Success(albums))
            }
    }

    override fun addAlbum(album: AlbumModel, result: (UiState<Pair<AlbumModel, String>>) -> Unit) {
        val ref = database.reference.child(FireDatabase.GALLERY).push()
        val uniqueKey = ref.key ?: "invalid"
        album.key = uniqueKey
        ref
            .setValue(album)
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(album, "Альбом успешно добавлен")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun updateAlbum(
        album: AlbumModel,
        result: (UiState<Pair<AlbumModel, String>>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.GALLERY).child(album.key.toString())
        ref
            .setValue(album)
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(album, "Альбом успешно обновлен")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun deleteAlbum(
        album: AlbumModel,
        result: (UiState<Pair<AlbumModel, String>>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.GALLERY).child(album.key.toString())
        ref
            .removeValue()
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(album, "Альбом успешно удален")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
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
            onResult.invoke(UiState.Success(urls))
        } catch (e: FirebaseException) {
            onResult.invoke(UiState.Failure(e.message))
        } catch (e: Exception) {
            onResult.invoke(UiState.Failure(e.message))
        }
    }
}