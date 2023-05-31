package com.example.dacha.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacha.data.model.NewsModel
import com.example.dacha.data.model.PersonModel
import com.example.dacha.data.repository.HomeRepository
import com.example.dacha.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: HomeRepository) : ViewModel() {
    private val _addPerson = MutableLiveData<UiState<PersonModel>>()
    val addPerson: LiveData<UiState<PersonModel>> = _addPerson

    fun addPerson(name: String) {
        _addPerson.value = UiState.Loading
        repository.addPerson(name) {_addPerson.value = it}
    }

    private val _choosePerson = MutableLiveData<UiState<PersonModel>>()
    val choosePerson: LiveData<UiState<PersonModel>> = _choosePerson

    fun choosePerson(person: PersonModel) {
        _choosePerson.value = UiState.Loading
        repository.choosePerson(person) {_choosePerson.value = it}
    }

    private val _people = MutableLiveData<UiState<List<PersonModel>>>()
    val people: LiveData<UiState<List<PersonModel>>> = _people

    fun getPeople() = viewModelScope.launch {
        _people.value = UiState.Loading
        val result = repository.getPeople()
        _people.value = result
    }

    private val _logout = MutableLiveData<UiState<PersonModel>>()
    val logout: LiveData<UiState<PersonModel>> = _logout

    fun logout(person: PersonModel) {
        _logout.value = UiState.Loading
        repository.logout(person) {_logout.value = it}
    }

    private val _person = MutableLiveData<UiState<PersonModel?>>()
    val person: LiveData<UiState<PersonModel?>> = _person

    fun getPerson(){
        repository.getPerson {_person.value = it}
    }

    fun addNews(news: NewsModel) {
        repository.addNews(news) {}
    }

    private val _news = MutableLiveData<UiState<List<NewsModel>>>()
    val news: LiveData<UiState<List<NewsModel>>> = _news
    fun getNews() = viewModelScope.launch {
        _news.value = UiState.Loading
        val result = repository.getNews()
        _news.value = result
    }
}