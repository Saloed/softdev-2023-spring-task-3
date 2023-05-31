package com.example.mytarget

import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val TASKS_COLLECTION_REF = "tasks"

class StorageRepo {
    fun hasUser(): Boolean = Firebase.auth.currentUser != null

    private val tasksRef: CollectionReference = Firebase.firestore.collection(TASKS_COLLECTION_REF)

    fun getUserTasks(
        userId: String
    ): Flow<Resources<List<Task>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = tasksRef
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val tasks = snapshot.toObjects(Task::class.java)
                        Resources.Success(data = tasks)
                    } else {
                        Resources.Error(throwable = e?.cause)
                    }
                    trySend(response)

                }


        } catch (e: Exception) {
            trySend(Resources.Error(e.cause))
            e.printStackTrace()
        }

        awaitClose {
            snapshotStateListener?.remove()
        }

    }

    fun addTask(
        userId: String,
        taskName: String,
        taskDescription: String,
        date: Timestamp,
        taskDone: Boolean = false,
        taskColor: Int
    ) {
        val documentId = tasksRef.document().id
        val taskAdd = Task(
            userId = userId,
            date = date,
            taskName = taskName,
            taskDescription = taskDescription,
            taskDone = taskDone,
            taskColor = taskColor,
            documentId = documentId
        )
        tasksRef
            .document(documentId)
            .set(taskAdd)
    }

    fun deleteTask(taskId: String) {
        tasksRef.document(taskId)
            .delete()
    }

    fun updateTask(
        taskId: String,
        taskDone: Boolean
    ) {
        val updateData = hashMapOf<String, Any>(
            "taskDone" to taskDone,
        )

        tasksRef.document("/$taskId").update(updateData)
    }

    sealed class Resources<T>(val data: T? = null, val throwable: Throwable? = null) {
        class Loading<T> : Resources<T>()
        class Success<T>(data: T?) : Resources<T>(data = data)
        class Error<T>(throwable: Throwable?) : Resources<T>(throwable = throwable)
    }
}

