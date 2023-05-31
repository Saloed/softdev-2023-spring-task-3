package com.example.dacha.data.repository

import com.example.dacha.data.model.PersonModel
import com.example.dacha.utils.FireDatabase
import com.example.dacha.utils.UiState
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await


class PersonRepositoryImpl(val database: FirebaseDatabase) : PersonRepository {
    override fun addPerson(
        person: PersonModel,
        result: (UiState<PersonModel>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.PEOPLE).push()
        val uniqueKey = ref.key ?: "invalid"
        person.id = uniqueKey
        ref
            .setValue(person)
            .addOnSuccessListener {
                result(UiState.Success(person))
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override fun updatePerson(
        person: PersonModel,
        result: (UiState<PersonModel>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.PEOPLE).child(person.id.toString())
        ref
            .setValue(person)
            .addOnSuccessListener {
                result(UiState.Success(person))
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override fun deletePerson(
        person: PersonModel,
        result: (UiState<PersonModel>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.PEOPLE).child(person.id.toString())
        ref
            .removeValue()
            .addOnSuccessListener {
                result(UiState.Success(person))
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override suspend fun getPeople(): UiState<List<PersonModel>> {
        val ref = database.reference.child(FireDatabase.PEOPLE)
        val people = arrayListOf<PersonModel>()
        ref.get()
            .addOnSuccessListener {
                for (item in it.children) {
                    val person = item.getValue(PersonModel::class.java)
                    if (person != null) people.add(person)
                }
            }.await()
        return UiState.Success(people)
    }
}