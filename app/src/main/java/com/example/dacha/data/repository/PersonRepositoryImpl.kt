package com.example.dacha.data.repository

import com.example.dacha.data.model.PersonModel
import com.example.dacha.utils.FireDatabase
import com.example.dacha.utils.UiState
import com.google.firebase.database.FirebaseDatabase


class PersonRepositoryImpl(val database: FirebaseDatabase) : PersonRepository {
    override fun addPerson(
        person: PersonModel,
        result: (UiState<Pair<PersonModel, String>>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.PEOPLE).push()
        val uniqueKey = ref.key ?: "invalid"
        person.id = uniqueKey
        ref
            .setValue(person)
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(person, "Человек успешно добавлен")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun updatePerson(
        person: PersonModel,
        result: (UiState<Pair<PersonModel, String>>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.PEOPLE).child(person.id.toString())
        ref
            .setValue(person)
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(person, "Данные успешно обновлены")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun deletePerson(
        person: PersonModel,
        result: (UiState<Pair<PersonModel, String>>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.PEOPLE).child(person.id.toString())
        ref
            .removeValue()
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(person, "Человек успешно удален")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun getPeople(
        result: (UiState<List<PersonModel>>) -> Unit
    ) {
        val ref = database.reference.child(FireDatabase.PEOPLE)
        ref.get()
            .addOnSuccessListener {
                val people = arrayListOf<PersonModel>()
                for (item in it.children) {
                    val person = item.getValue(PersonModel::class.java)
                    if (person != null) people.add(person)
                }
                result.invoke(UiState.Success(people))
            }
    }
}