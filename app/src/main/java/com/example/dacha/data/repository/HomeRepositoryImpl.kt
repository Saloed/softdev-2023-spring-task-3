package com.example.dacha.data.repository

import android.content.SharedPreferences
import com.example.dacha.data.model.NewsModel
import com.example.dacha.data.model.PersonModel
import com.example.dacha.utils.FireDatabase
import com.example.dacha.utils.SharedPrefConstants
import com.example.dacha.utils.UiState
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class HomeRepositoryImpl(val database: FirebaseDatabase, private val shPref: SharedPreferences) :
    HomeRepository {
    override fun addPerson(name: String, result: (UiState<PersonModel>) -> Unit) {
        val ref = database.reference.child(FireDatabase.PEOPLE).push()
        val uniqueKey = ref.key ?: "invalid"
        val person = PersonModel(null, uniqueKey, name, null)
        ref
            .setValue(person)
            .addOnSuccessListener {
                savePerson(person) { result(it) }
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override fun choosePerson(
        person: PersonModel,
        result: (UiState<PersonModel>) -> Unit
    ) {
        savePerson(person) { result(it) }
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

    override fun savePerson(
        person: PersonModel,
        result: (UiState<PersonModel>) -> Unit
    ) {
        shPref.edit().putString(SharedPrefConstants.NAME, person.name)
            .putString(SharedPrefConstants.ID, person.id).apply()
        result(UiState.Success(person))
    }

    override fun logout(person: PersonModel, result: (UiState<PersonModel>) -> Unit) {
        shPref.edit().putString(SharedPrefConstants.NAME, null)
            .putString(SharedPrefConstants.ID, null).apply()
        result(UiState.Success(person))
    }

    override fun getPerson(result: (UiState<PersonModel?>) -> Unit) {
        val name = shPref.getString(SharedPrefConstants.NAME, null)
        val id = shPref.getString(SharedPrefConstants.ID, null)
        if (id == null) {
            result(UiState.Success(null))
        } else {
            result(UiState.Success(PersonModel(null, id, name, null)))
        }
    }

    override fun addNews(news: NewsModel, result: (UiState<NewsModel>) -> Unit) {
        val ref = database.reference.child(FireDatabase.NEWS).push()
        val uniqueKey = ref.key ?: "invalid"
        val new =
            NewsModel(key = uniqueKey, person = news.person, info = news.info, date = news.date)
        ref
            .setValue(new)
            .addOnSuccessListener {
                result(UiState.Success(new))
            }
            .addOnFailureListener {
                result(UiState.Failure(it.localizedMessage))
            }
    }

    override suspend fun getNews(): UiState<List<NewsModel>> {
        val ref = database.reference.child(FireDatabase.NEWS)
        val news = arrayListOf<NewsModel>()
        ref.get()
            .addOnSuccessListener {
                for (item in it.children) {
                    val new = item.getValue(NewsModel::class.java)
                    if (new != null) news.add(new)
                }
            }.await()
        return UiState.Success(news)
    }
}