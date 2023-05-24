package com.example.dacha.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dacha.data.model.PersonModel
import com.example.dacha.data.repository.HomeRepository
import com.example.dacha.data.repository.PersonRepository
import com.example.dacha.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: HomeRepository) : ViewModel() {
    private val _addPerson = MutableLiveData<UiState<Pair<PersonModel, String>>>()
    val addPerson: LiveData<UiState<Pair<PersonModel, String>>> = _addPerson

    fun addPerson(name: String) {
        _addPerson.value = UiState.Loading
        repository.addPerson(name) {_addPerson.value = it}
    }

    private val _choosePerson = MutableLiveData<UiState<Pair<PersonModel, String>>>()
    val choosePerson: LiveData<UiState<Pair<PersonModel, String>>> = _choosePerson

    fun choosePerson(person: PersonModel) {
        _choosePerson.value = UiState.Loading
        repository.choosePerson(person) {_choosePerson.value = it}
    }

    private val _people = MutableLiveData<UiState<List<PersonModel>>>()
    val people: LiveData<UiState<List<PersonModel>>> = _people

    fun getPeople() {
        _people.value = UiState.Loading
        repository.getPeople {_people.value = it}
    }

    private val _logout = MutableLiveData<UiState<Pair<PersonModel, String>>>()
    val logout: LiveData<UiState<Pair<PersonModel, String>>> = _logout

    fun logout(person: PersonModel) {
        _logout.value = UiState.Loading
        repository.logout(person) {_logout.value = it}
    }

    private val _person = MutableLiveData<UiState<PersonModel?>>()
    val person: LiveData<UiState<PersonModel?>> = _person

    fun getPerson(){
        repository.getPerson {_person.value = it}
    }
}