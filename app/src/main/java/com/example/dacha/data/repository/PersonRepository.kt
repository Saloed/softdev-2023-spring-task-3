package com.example.dacha.data.repository

import com.example.dacha.data.model.PersonModel
import com.example.dacha.utils.UiState

interface PersonRepository {
    fun addPerson(person: PersonModel, result: (UiState<PersonModel>) -> Unit)
    fun updatePerson(person: PersonModel, result: (UiState<PersonModel>) -> Unit)
    fun deletePerson(person: PersonModel, result: (UiState<PersonModel>) -> Unit)
    suspend fun getPeople(): UiState<List<PersonModel>>
}