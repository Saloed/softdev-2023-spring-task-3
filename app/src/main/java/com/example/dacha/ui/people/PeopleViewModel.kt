package com.example.dacha.ui.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dacha.data.model.PersonModel
import com.example.dacha.data.repository.PersonRepository
import com.example.dacha.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(val repository: PersonRepository) : ViewModel() {
    private val _addPerson = MutableLiveData<UiState<PersonModel>>()
    val addPerson: LiveData<UiState<PersonModel>> = _addPerson

    private val _updatePerson = MutableLiveData<UiState<PersonModel>>()
    val updatePerson: LiveData<UiState<PersonModel>> = _updatePerson

    private val _deletePerson = MutableLiveData<UiState<PersonModel>>()
    val deletePerson: LiveData<UiState<PersonModel>> = _deletePerson

    private val _people = MutableLiveData<UiState<List<PersonModel>>>()
    val people: LiveData<UiState<List<PersonModel>>> = _people

    fun addPerson(person: PersonModel) {
        _addPerson.value = UiState.Loading
        repository.addPerson(person) {_addPerson.value = it}
    }

    fun updatePerson(person: PersonModel) {
        _updatePerson.value = UiState.Loading
        repository.updatePerson(person) {_updatePerson.value = it}
    }

    fun deletePerson(person: PersonModel) {
        _deletePerson.value = UiState.Loading
        repository.deletePerson(person) {_deletePerson.value = it}
    }

    fun getPeople() = viewModelScope.launch {
        _people.value = UiState.Loading
        val result = repository.getPeople()
        _people.value = result
    }
}