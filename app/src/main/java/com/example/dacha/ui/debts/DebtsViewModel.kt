package com.example.dacha.ui.debts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dacha.data.model.EventModel
import com.example.dacha.data.model.PersonModel
import com.example.dacha.data.model.TransactionModel
import com.example.dacha.data.repository.DebtRepository
import com.example.dacha.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DebtsViewModel @Inject constructor(val repository: DebtRepository) : ViewModel() {

    private val _events = MutableLiveData<UiState<List<EventModel>>>()
    val events: LiveData<UiState<List<EventModel>>> = _events

    fun getEvents() {
        _events.value = UiState.Loading
        repository.getEvents {_events.value = it}
    }

    private val _people = MutableLiveData<UiState<List<PersonModel>>>()
    val people: LiveData<UiState<List<PersonModel>>> = _people

    fun getPeople() {
        _people.value = UiState.Loading
        repository.getPeople {_people.value = it}
    }

    private val _addTransaction = MutableLiveData<UiState<Pair<TransactionModel, String>>>()
    val addTransaction: LiveData<UiState<Pair<TransactionModel, String>>> = _addTransaction


    private val _deleteTransaction = MutableLiveData<UiState<Pair<TransactionModel, String>>>()
    val deleteTransaction: LiveData<UiState<Pair<TransactionModel, String>>> = _deleteTransaction

    private val _transactions = MutableLiveData<UiState<List<TransactionModel>>>()
    val transactions: LiveData<UiState<List<TransactionModel>>> = _transactions

    fun addTransaction(transaction: TransactionModel) {
        _addTransaction.value = UiState.Loading
        repository.addTransaction(transaction) {_addTransaction.value = it}
    }

    fun deleteTransaction(transaction: TransactionModel) {
        _deleteTransaction.value = UiState.Loading
        repository.deleteTransaction(transaction) {_deleteTransaction.value = it}
    }

    fun getTransactions() {
        _transactions.value = UiState.Loading
        repository.getTransactions {_transactions.value = it}
    }

}