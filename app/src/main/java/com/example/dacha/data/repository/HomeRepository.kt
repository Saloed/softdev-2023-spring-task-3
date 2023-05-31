package com.example.dacha.data.repository

import com.example.dacha.data.model.NewsModel
import com.example.dacha.data.model.PersonModel
import com.example.dacha.utils.UiState

interface HomeRepository {
    fun addPerson(name: String, result: (UiState<PersonModel>) -> Unit)
    fun choosePerson(person: PersonModel, result: (UiState<PersonModel>) -> Unit)
    suspend fun getPeople(): UiState<List<PersonModel>>
    fun savePerson(person: PersonModel, result: (UiState<PersonModel>) -> Unit)
    fun logout(person: PersonModel, result: (UiState<PersonModel>) -> Unit)
    fun getPerson(result: (UiState<PersonModel?>) -> Unit)
    fun addNews(news: NewsModel, result: (UiState<NewsModel>) -> Unit)
    suspend fun getNews(): UiState<List<NewsModel>>
}