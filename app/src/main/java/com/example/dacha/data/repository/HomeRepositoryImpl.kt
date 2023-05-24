package com.example.dacha.data.repository

import android.content.SharedPreferences
import android.media.metrics.PlaybackErrorEvent
import com.example.dacha.data.model.PersonModel
import com.example.dacha.utils.FireDatabase
import com.example.dacha.utils.SharedPrefConstants
import com.example.dacha.utils.UiState
import com.google.firebase.database.FirebaseDatabase

class HomeRepositoryImpl(val database: FirebaseDatabase, val shPref: SharedPreferences) :
    HomeRepository {
    override fun addPerson(name: String, result: (UiState<Pair<PersonModel, String>>) -> Unit) {
        val ref = database.reference.child(FireDatabase.PEOPLE).push()
        val uniqueKey = ref.key ?: "invalid"
        val person = PersonModel(null, uniqueKey, name, null)
        ref
            .setValue(person)
            .addOnSuccessListener {
                savePerson(person) { result.invoke(it) }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun choosePerson(
        person: PersonModel,
        result: (UiState<Pair<PersonModel, String>>) -> Unit
    ) {
        savePerson(person) { result.invoke(it) }
    }

    override fun getPeople(result: (UiState<List<PersonModel>>) -> Unit) {
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

    override fun savePerson(
        person: PersonModel,
        result: (UiState<Pair<PersonModel, String>>) -> Unit
    ) {
        shPref.edit().putString(SharedPrefConstants.NAME, person.name)
            .putString(SharedPrefConstants.ID, person.id).apply()
        result.invoke(UiState.Success(Pair(person, "Данные сохранены")))
    }

    override fun logout(person: PersonModel, result: (UiState<Pair<PersonModel, String>>) -> Unit) {
        shPref.edit().putString(SharedPrefConstants.NAME, null)
            .putString(SharedPrefConstants.ID, null).apply()
        result.invoke(UiState.Success(Pair(person, "Вы успешно вышли")))
    }

    override fun getPerson(result: (UiState<PersonModel?>) -> Unit) {
        val name = shPref.getString(SharedPrefConstants.NAME, null)
        val id = shPref.getString(SharedPrefConstants.ID, null)
        if (id == null) {
            result.invoke(UiState.Success(null))
        } else {
            result.invoke(UiState.Success(PersonModel(null, id, name, null)))
        }
    }
}