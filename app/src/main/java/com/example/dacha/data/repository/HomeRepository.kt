package com.example.dacha.data.repository

import com.example.dacha.data.model.PersonModel
import com.example.dacha.utils.UiState

interface HomeRepository {
    fun addPerson(name: String, result: (UiState<Pair<PersonModel, String>>) -> Unit)
    fun choosePerson(person: PersonModel, result: (UiState<Pair<PersonModel, String>>) -> Unit)
    fun getPeople(result: (UiState<List<PersonModel>>) -> Unit)
    fun savePerson(person: PersonModel, result: (UiState<Pair<PersonModel, String>>) -> Unit)
    fun logout(person: PersonModel, result: (UiState<Pair<PersonModel, String>>) -> Unit)
    fun getPerson(result: (UiState<PersonModel?>) -> Unit)
}